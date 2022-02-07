package ru.popov.shogi.classes

import android.app.Activity
import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import com.google.gson.Gson
import kotlinx.coroutines.*
import okhttp3.WebSocket
import ru.popov.shogi.R
import ru.popov.shogi.classes.figures.*
import kotlin.concurrent.thread

class ShogiModelMultiplayer(var orientation: Orientation, var top:Float, var left:Float, var noteSize:Int, var separateLineSize:Int, var layout: RelativeLayout,
                            private var context: Activity, private var layoutParams: ViewGroup.LayoutParams, var topBoard: Float, var leftBoard: Float,var webSocket: WebSocket,
                             private var yourSide: Side,private var turn:Side) {
    private val figuresOnBoard:MutableSet<Figure> = mutableSetOf()
    private val handWhite:ArrayList<Figure> = ArrayList()
    private val handBlack:ArrayList<Figure> = ArrayList()
    private val whiteBundle:BundleView
    private val blackBundle:BundleView
    private val whiteCounts:CountsView
    private val blackCounts:CountsView
    private var boardShogi:BoardArray = BoardArray()
    private var gson:Gson = Gson()

    companion object {
        private var promID:HashMap<String,PromotableIDs> = HashMap()
        init {
            promID["Pawn"] = PromotableIDs.PAWN
            promID["Lance"] = PromotableIDs.LANCE
            promID["Knight"] = PromotableIDs.KNIGHT
            promID["Silver"] = PromotableIDs.SILVER
            promID["Bishop"] = PromotableIDs.BISHOP
            promID["Rook"] = PromotableIDs.ROOK
        }
    }

    init {

        if (orientation == Orientation.NORMAL) {
            whiteCounts = layout.findViewById(R.id.bottom_counts)
            blackCounts = layout.findViewById(R.id.upper_counts)
            whiteBundle = layout.findViewById(R.id.lower_bundle)
            blackBundle = layout.findViewById(R.id.upper_bundle)
        } else {
            blackCounts = layout.findViewById(R.id.bottom_counts)
            whiteCounts = layout.findViewById(R.id.upper_counts)
            blackBundle = layout.findViewById(R.id.lower_bundle)
            whiteBundle = layout.findViewById(R.id.upper_bundle)
        }
        reset()
    }

    private fun getXScale(orientation: Orientation):Int{
        return if (orientation == Orientation.NORMAL) {
            1
        } else {
            -1
        }
    }

    private fun getYScale(orientation: Orientation):Int{
        return if (orientation == Orientation.NORMAL) {
            -1
        } else {
            -1
        }
    }

    private suspend fun searchRoots(figure: Figure):HashSet<Pair<Int,Int>> {
        val moves:HashSet<Pair<Int,Int>> = HashSet()
        var currentX = figure.col
        var currentY = figure.row
        val rules = figure.rules
        var list:ArrayList<Pair<Int,Int>> = ArrayList()
        if(figure.eaten){
            when(figure::class.java.simpleName) {
                "Pawn" -> {
                    for (i in 1..9){
                        for (j in 1..9){
                            if (j==1 || j ==9){
                                if (j==1 && figure.side == Side.BLACK)
                                    continue
                                if (j==9 && figure.side == Side.WHITE)
                                    continue
                            }
                            var it = boardShogi[i,j]
                            if (it != null){
                                if (it.javaClass.simpleName == "Pawn" && !it.promoted && it.side == figure.side){
                                    list.clear()
                                    break
                                }
                            } else {
                                list.add(Pair(i,j))
                            }
                        }
                        list.forEach { moves.add(it) }
                    }
                }
                "Knight"-> {
                    var k = 1
                    var m = 9
                    if (figure.side == Side.WHITE) m =7
                    else k = 3
                    for (i in 1..9){
                        for (j in k..m){
                            if (boardShogi[i,j] == null) {
                                moves.add(Pair(i,j))
                            }
                        }
                    }
                }
                "Lance" -> {
                    var k = 1
                    var m = 9
                    if (figure.side == Side.WHITE) m = 8
                    else k = 2
                    for (i in 1..9){
                        for (j in k..m){
                            if (boardShogi[i,j] == null) {
                                moves.add(Pair(i,j))
                            }
                        }
                    }
                }
                else -> {
                    for (i in 1..9)
                        for (j in 1..9)
                            if (boardShogi[i,j] == null)
                                moves.add(Pair(i,j))
                }
            }
        } else {
            for (i in 0 until rules.size){
                val vectorMove = rules[i]
                var x = vectorMove.vector.first
                var y = vectorMove.vector.second
                var length = 0
                var isEat = false
                for(j in 0 until vectorMove.length){
                    if (currentX + x <1 || currentX + x > 9 || currentY + y < 1 || currentY + y >9 ||
                        boardShogi[currentX + x,currentY + y]?.side == figure.side || isEat) break

                    boardShogi[currentX + x, currentY + y]?.let {
                        isEat = it.side != figure.side
                    }
                    currentX+=x
                    currentY+=y
                    moves.add(Pair(currentX,currentY))
                    length++
                }
                currentX= figure.col
                currentY = figure.row
            }
        }
        return moves
    }


    fun movePieceAt(displacementInfo: DisplacementInfo){
        var figure = boardShogi[displacementInfo.fromCol,displacementInfo.fromRow]
        var vectorX:Int = displacementInfo.toCol - displacementInfo.fromCol
        var vectorY:Int = displacementInfo.toRow - displacementInfo.fromRow
        var deltaX:Int = 0
        var deltaY:Int = 0
        if (orientation == Orientation.NORMAL) {
            deltaX = vectorX * (noteSize + separateLineSize)
            deltaY = -vectorY * (noteSize + separateLineSize)
        } else {
            deltaX = -vectorX * (noteSize + separateLineSize)
            deltaY = vectorY * (noteSize + separateLineSize)
        }
        figure?.let {
            context.runOnUiThread {
                if (displacementInfo.promote){
                    it.promote()
                }
                movePieceAt(it,displacementInfo.toRow,displacementInfo.toCol,it.pieceImage.x + deltaX,it.pieceImage.y + deltaY)
            }
        }
    }
    private fun movePieceAt(figure: Figure, row:Int, col:Int, x:Float, y:Float){
        boardShogi[col,row]?.also {
            it.changeSide()
            it.row = 0
            it.col = 0

            // Move to bundle
            if (it.side == Side.WHITE) {
                whiteBundle.moveToBundle(it)
                whiteCounts.increment(it)
            } else {
                blackCounts.increment(it)
                blackBundle.moveToBundle(it)
            }
            it.eaten = true
        }
        if (figure.eaten){
            if(figure.side == Side.WHITE){
                whiteCounts.decrement(figure)
            } else {
                blackCounts.decrement(figure)
            }
            figure.eaten = false
            figure.pieceImage.layoutParams = RelativeLayout.LayoutParams(layoutParams.width,layoutParams.height)
        } else {
            boardShogi[figure.col,figure.row] = null
        }

        boardShogi[col,row] = figure
        figure.row = row
        figure.col = col
        turn = turn.next()
        Log.i("Piece",figure.pieceImage.x.toString()+" "+figure.pieceImage.y)
        figure.pieceImage.x = x
        figure.pieceImage.y = y
        figure.pieceImage.colorFilter = ColorMatrixColorFilter(ColorMatrix())
    }

    private fun reset(){
        figuresOnBoard.clear();
        handBlack.clear()
        handWhite.clear()


        val appInfo = AppInfo(context, layout, layoutParams)
        val delta = separateLineSize + noteSize
        val scaleX  = getXScale(orientation)
        val scaleY = getYScale(orientation)
        var firstCellX = 0f
        var firstCellY = 0f
        if (orientation == Orientation.NORMAL){
            firstCellX = left
            firstCellY = top + delta * 8
        } else {
            firstCellX = left + 8 * delta
            firstCellY = top
        }
        var startPromoting = false
        var moves: Deferred<HashSet<Pair<Int, Int>>>? = null
        var checkedView: View? = null
        var moveInfo:MoveInfo? = null
        var chooseToPromoteOld: ImageView = ImageView(context)
        var chooseToPromoteNew: ImageView = ImageView(context)
        chooseToPromoteOld.setOnClickListener {
            checkedView = null
            startPromoting = false
            moveInfo?.let {
                thread {
                    webSocket.send(gson.toJson(DisplacementInfo(it.figure.col,it.figure.row,it.col,it.row,false)))
                }
                movePieceAt(it.figure,it.row,it.col,it.x,it.y)
            }
            appInfo.layout.removeView(chooseToPromoteNew)
            appInfo.layout.removeView(chooseToPromoteOld)
            layout.findViewById<WhiteCanvasView>(R.id.white_canvas).also {
                it.clean = true
                it.invalidate()
            }
        }

        chooseToPromoteNew.setOnClickListener {
            checkedView = null
            startPromoting = false
            moveInfo?.let {
                thread {
                    webSocket.send(gson.toJson(DisplacementInfo(it.figure.col,it.figure.row,it.col,it.row,true)))
                }
                it.figure.promote()
                movePieceAt(it.figure,it.row,it.col,it.x,it.y)
            }
            appInfo.layout.removeView(chooseToPromoteNew)
            appInfo.layout.removeView(chooseToPromoteOld)
            layout.findViewById<WhiteCanvasView>(R.id.white_canvas).also {
                it.clean = true
                it.invalidate()
            }
        }



        var firstTouch = false
        var startFirstCellX = if (orientation == Orientation.NORMAL) {
            leftBoard + separateLineSize
        } else {
            leftBoard + 9 * delta
        }

        var startFirstCellY = if (orientation == Orientation.NORMAL) {
            topBoard + 9 * delta
        } else {
            topBoard + separateLineSize
        }
        val inside = 9 * noteSize + 8 * separateLineSize
        val finishLastCellX = startFirstCellX + scaleX * inside
        val finishLastCellY = startFirstCellY + scaleY * inside

        layout.findViewById<ViewMoves>(R.id.available_moves).also {
            it.boardArray = boardShogi
        }

        var oldX:Float = 0f
        var oldY:Float = 0f

        val touchBoard: View.OnTouchListener = View.OnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if(checkedView != null && !startPromoting) {
                        var x:Int = event.x.toInt()
                        var y:Int = event.y.toInt()
                        if (orientation == Orientation.NORMAL){
                            if(x >= startFirstCellX && x <= finishLastCellX && y <= startFirstCellY && y >= finishLastCellY) {
                                var deltaX:Int = x - startFirstCellX.toInt()
                                var deltaY:Int = startFirstCellY.toInt() - y
                                var plusX = deltaX / delta
                                var plusY = deltaY / delta
                                var relativeX = x - (startFirstCellX + plusX * delta)
                                var relativeY = startFirstCellY - plusY * delta - y
                                if (relativeX <= noteSize && relativeY <= noteSize){
                                    runBlocking {
                                        if(moves?.await()?.contains(Pair(plusX + 1, plusY + 1)) == true) {
                                            var coordinateX = startFirstCellX + plusX * delta  + noteSize / 2  - layoutParams.width / 2
                                            var coordinateY = startFirstCellY - plusY * delta  - noteSize / 2  - layoutParams.height / 2
                                            var figure =(checkedView as PieceView).figure
                                            var promoting = figure.ableToPromote(plusY + 1)
                                            when(promoting){
                                                Promoting.NECESSARY -> {
                                                    var dislacemnt = DisplacementInfo(figure.col,figure.row,plusX+1,plusY+1,true)
                                                    thread {
                                                        webSocket.send(gson.toJson(dislacemnt))
                                                    }
                                                    figure.promote()
                                                    movePieceAt((checkedView as PieceView).figure,plusY + 1,plusX+1,coordinateX,coordinateY)
                                                    checkedView = null
                                                    layout.findViewById<ViewMoves>(R.id.available_moves).also {
                                                        it.clean = true
                                                        it.invalidate()
                                                    }
                                                }
                                                Promoting.ABLE -> {
                                                    oldX = figure.pieceImage.x
                                                    oldY = figure.pieceImage.y
                                                    chooseToPromoteNew.x = coordinateX
                                                    chooseToPromoteNew.y = coordinateY
                                                    promID[figure::class.simpleName]?.let {
                                                        if (figure.side == Side.BLACK) {
                                                            chooseToPromoteNew.setImageResource(it.promotedDown)
                                                            chooseToPromoteOld.setImageResource(it.commonDown)
                                                            chooseToPromoteOld.x = coordinateX
                                                            chooseToPromoteOld.y = coordinateY - noteSize - separateLineSize
                                                        } else {
                                                            chooseToPromoteNew.setImageResource(it.promotedUP)
                                                            chooseToPromoteOld.setImageResource(it.commonUP)
                                                            chooseToPromoteOld.x = coordinateX
                                                            chooseToPromoteOld.y = coordinateY + noteSize + separateLineSize
                                                        }
                                                    }
                                                    layout.findViewById<WhiteCanvasView>(R.id.white_canvas).also {
                                                        it.bringToFront()
                                                        it.clean = false
                                                        it.newPoint = Pair(chooseToPromoteNew.x,chooseToPromoteNew.y)
                                                        it.otherPoint = Pair(chooseToPromoteOld.x,chooseToPromoteOld.y)
                                                        it.layoutParam = figure.pieceImage.layoutParams
                                                        it.invalidate()
                                                    }
                                                    startPromoting = true
                                                    chooseToPromoteNew.layoutParams = figure.pieceImage.layoutParams
                                                    appInfo.layout.addView(chooseToPromoteNew)
                                                    chooseToPromoteOld.layoutParams = figure.pieceImage.layoutParams
                                                    appInfo.layout.addView(chooseToPromoteOld)
                                                    layout.findViewById<ViewMoves>(R.id.available_moves).also {
                                                        it.clean = true
                                                        it.invalidate()
                                                    }

                                                    figure.pieceImage.x = coordinateX
                                                    figure.pieceImage.y = coordinateY
                                                    moveInfo = MoveInfo(figure,plusX+1,plusY+1,coordinateX,coordinateY)
                                                    // Drawing 2 figures and canvas
                                                }
                                                Promoting.NONE -> {
                                                    var dislacemnt = DisplacementInfo(figure.col,figure.row,plusX+1,plusY+1,false)
                                                    thread {
                                                        webSocket.send(gson.toJson(dislacemnt))
                                                    }
                                                    movePieceAt((checkedView as PieceView).figure,plusY + 1,plusX+1,coordinateX,coordinateY)
                                                    checkedView = null
                                                    layout.findViewById<ViewMoves>(R.id.available_moves).also {
                                                        it.clean = true
                                                        it.invalidate()
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            if(x >= startFirstCellX && x <= finishLastCellX && y <= startFirstCellY && y >= finishLastCellY) {
                                var deltaX:Int = startFirstCellX.toInt() - x
                                var deltaY:Int = y - startFirstCellY.toInt()
                                var plusX = deltaX / delta
                                var plusY = deltaY / delta
                                var relativeX = startFirstCellX - plusX * delta - x
                                var relativeY = y - (startFirstCellY - plusY * delta)
                                if (relativeX <= noteSize && relativeY <= noteSize){
                                    GlobalScope.launch {
                                        if(moves?.await()?.contains(Pair(plusX + 1, plusY + 1)) == true) {
                                            var coordinateX = startFirstCellX - plusX * delta  - noteSize / 2  - layoutParams.width / 2
                                            var coordinateY = startFirstCellY + plusY * delta  + noteSize / 2  - layoutParams.height / 2
                                            var figure =(checkedView as PieceView).figure
                                            var promoting = figure.ableToPromote(plusY + 1)
                                            when(promoting){
                                                Promoting.NECESSARY -> {
                                                    var dislacemnt = DisplacementInfo(figure.col,figure.row,plusX+1,plusY+1,true)
                                                    thread {
                                                        webSocket.send(gson.toJson(dislacemnt))
                                                    }
                                                    figure.promote()
                                                    movePieceAt((checkedView as PieceView).figure,plusY + 1,plusX+1,coordinateX,coordinateY)
                                                    checkedView = null
                                                    layout.findViewById<ViewMoves>(R.id.available_moves).also {
                                                        it.clean = true
                                                        it.invalidate()
                                                    }
                                                }
                                                Promoting.ABLE -> {
                                                    oldX = figure.pieceImage.x
                                                    oldY = figure.pieceImage.y
                                                    chooseToPromoteNew.x = coordinateX
                                                    chooseToPromoteNew.y = coordinateY
                                                    promID[figure::class.simpleName]?.let {
                                                        if (figure.side == Side.WHITE) {
                                                            chooseToPromoteNew.setImageResource(it.promotedDown)
                                                            chooseToPromoteOld.setImageResource(it.commonDown)
                                                            chooseToPromoteOld.x = coordinateX
                                                            chooseToPromoteOld.y = coordinateY - noteSize - separateLineSize
                                                        } else {
                                                            chooseToPromoteNew.setImageResource(it.promotedUP)
                                                            chooseToPromoteOld.setImageResource(it.commonUP)
                                                            chooseToPromoteOld.x = coordinateX
                                                            chooseToPromoteOld.y = coordinateY + noteSize + separateLineSize
                                                        }
                                                    }
                                                    layout.findViewById<WhiteCanvasView>(R.id.white_canvas).also {
                                                        it.bringToFront()
                                                        it.clean = false
                                                        it.newPoint = Pair(chooseToPromoteNew.x,chooseToPromoteNew.y)
                                                        it.otherPoint = Pair(chooseToPromoteOld.x,chooseToPromoteOld.y)
                                                        it.layoutParam = figure.pieceImage.layoutParams
                                                        it.invalidate()
                                                    }
                                                    startPromoting = true

                                                    chooseToPromoteNew.layoutParams = figure.pieceImage.layoutParams
                                                    appInfo.layout.addView(chooseToPromoteNew)
                                                    chooseToPromoteOld.layoutParams = figure.pieceImage.layoutParams
                                                    appInfo.layout.addView(chooseToPromoteOld)
                                                    layout.findViewById<ViewMoves>(R.id.available_moves).also {
                                                        it.clean = true
                                                        it.invalidate()
                                                    }
                                                    figure.pieceImage.x = coordinateX
                                                    figure.pieceImage.y = coordinateY
                                                    moveInfo = MoveInfo(figure,plusX+1,plusY+1,coordinateX,coordinateY)
                                                    // Drawing 2 figures and canvas
                                                }
                                                Promoting.NONE -> {
                                                    var dislacemnt = DisplacementInfo(figure.col,figure.row,plusX+1,plusY+1,false)
                                                    thread {
                                                        webSocket.send(gson.toJson(dislacemnt))
                                                    }
                                                    movePieceAt((checkedView as PieceView).figure,plusY + 1,plusX+1,coordinateX,coordinateY)
                                                    checkedView = null
                                                    layout.findViewById<ViewMoves>(R.id.available_moves).also {
                                                        it.clean = true
                                                        it.invalidate()
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            true
        }
        layout.findViewById<ShogiView>(R.id.shogi_view).setOnTouchListener(touchBoard)

        val touchListener: View.OnClickListener = View.OnClickListener { v->
            if (yourSide == turn && (v as PieceView).figure.side == turn && (checkedView == null || checkedView != v && (checkedView as PieceView).figure.side == (v as PieceView).figure.side) ) {
                if(checkedView != null && checkedView != v) {
                    layout.findViewById<ViewMoves>(R.id.available_moves).also {
                        it.clean = true
                        it.invalidate()
                    }
                    (checkedView as ImageView).colorFilter = ColorMatrixColorFilter(ColorMatrix())

                }
                if (startPromoting){
                    startPromoting = false
                    appInfo.layout.removeView(chooseToPromoteNew)
                    appInfo.layout.removeView(chooseToPromoteOld)
                    layout.findViewById<WhiteCanvasView>(R.id.white_canvas).also {
                        it.clean = true
                        it.invalidate()
                    }
                    (checkedView as PieceView).x = oldX
                    (checkedView as PieceView).y = oldY
                }
                checkedView = v
                firstTouch = false
                val matrixArr: FloatArray = floatArrayOf(0f,0f,0f,0f,0f,
                    0f,1f,0f,0f,0f,
                    0f,0f,0f,0f,0f,
                    0f,0f,0f,0.5f,0f,
                    0f,0f,0f,0f,0f)

                (v as ImageView).colorFilter = ColorMatrixColorFilter(ColorMatrix(matrixArr))
                GlobalScope.launch {
                    Log.i("COR","Start coroutine")
                    moves = async { searchRoots((v as PieceView).figure) }
                    val waitMoves = moves?.await()
                    if (waitMoves != null) {
                        layout.findViewById<ViewMoves>(R.id.available_moves).also {
                            it.movesSet = waitMoves
                            it.scaleX = scaleX
                            it.scaleY = scaleY
                            it.clean = false
                            it.invalidate()
                        }
                    }
                }
                Log.i("COR","OutSide Run")
            } else {
                if (checkedView != null && checkedView != v && !startPromoting) {
                    if ((checkedView as PieceView).figure.side != (v as PieceView).figure.side){
                        var selected = checkedView as PieceView
                        var enemy = v
                        runBlocking {
                            var w8Moves = moves?.await()
                            if(w8Moves?.contains(Pair(enemy.figure.col,enemy.figure.row)) == true) {
                                var figure = (checkedView as PieceView).figure
                                var enemyFigure = enemy.figure
                                var promoting = figure.ableToPromote(enemy.figure.row)
                                when(promoting) {
                                    Promoting.NECESSARY -> {
                                        var displacement = DisplacementInfo(figure.col,figure.row,enemyFigure.col,enemyFigure.row,true)
                                        thread {
                                            webSocket.send(gson.toJson(displacement))
                                        }
                                        figure.promote()
                                        movePieceAt(figure,enemyFigure.row,enemyFigure.col,enemy.x,enemy.y)
                                        layout.findViewById<ViewMoves>(R.id.available_moves).also {
                                            it.clean = true
                                            it.invalidate()
                                        }
                                        (checkedView as ImageView).colorFilter = ColorMatrixColorFilter(
                                            ColorMatrix()
                                        )
                                        checkedView = null
                                    }
                                    Promoting.ABLE -> {
                                        oldX = figure.pieceImage.x
                                        oldY = figure.pieceImage.y
                                        chooseToPromoteNew.x = enemy.x
                                        chooseToPromoteNew.y = enemy.y
                                        promID[figure::class.simpleName]?.let {
                                            if (figure.side == Side.WHITE && orientation == Orientation.REVERSE ||
                                                figure.side == Side.BLACK && orientation == Orientation.NORMAL) {
                                                chooseToPromoteNew.setImageResource(it.promotedDown)
                                                chooseToPromoteOld.setImageResource(it.commonDown)
                                                chooseToPromoteOld.x = enemy.x
                                                chooseToPromoteOld.y = enemy.y - noteSize - separateLineSize
                                            } else {
                                                chooseToPromoteNew.setImageResource(it.promotedUP)
                                                chooseToPromoteOld.setImageResource(it.commonUP)
                                                chooseToPromoteOld.x = enemy.x
                                                chooseToPromoteOld.y = enemy.y + noteSize + separateLineSize
                                            }
                                        }
                                        layout.findViewById<WhiteCanvasView>(R.id.white_canvas).also {
                                            it.bringToFront()
                                            it.clean = false
                                            it.newPoint = Pair(chooseToPromoteNew.x,chooseToPromoteNew.y)
                                            it.otherPoint = Pair(chooseToPromoteOld.x,chooseToPromoteOld.y)
                                            it.layoutParam = figure.pieceImage.layoutParams
                                            it.invalidate()
                                        }
                                        startPromoting = true

                                        chooseToPromoteNew.layoutParams = figure.pieceImage.layoutParams
                                        appInfo.layout.addView(chooseToPromoteNew)
                                        chooseToPromoteOld.layoutParams = figure.pieceImage.layoutParams
                                        appInfo.layout.addView(chooseToPromoteOld)
                                        layout.findViewById<ViewMoves>(R.id.available_moves).also {
                                            it.clean = true
                                            it.invalidate()
                                        }
                                        figure.pieceImage.x = enemy.x
                                        figure.pieceImage.y = enemy.y
                                        moveInfo = MoveInfo(figure,enemyFigure.col,enemyFigure.row,enemy.x,enemy.y)
                                    }
                                    Promoting.NONE -> {
                                        var displacement = DisplacementInfo(figure.col,figure.row,enemyFigure.col,enemyFigure.row,false)
                                        thread {
                                            webSocket.send(gson.toJson(displacement))
                                        }
                                        movePieceAt(figure,enemyFigure.row,enemyFigure.col,enemy.x,enemy.y)
                                        layout.findViewById<ViewMoves>(R.id.available_moves).also {
                                            it.clean = true
                                            it.invalidate()
                                        }
                                        (checkedView as ImageView).colorFilter = ColorMatrixColorFilter(
                                            ColorMatrix()
                                        )
                                        checkedView = null
                                    }
                                }

                            } else {
                                layout.findViewById<ViewMoves>(R.id.available_moves).also {
                                    it.clean = true
                                    it.invalidate()
                                    if (v == checkedView){
                                        (v as ImageView).colorFilter = ColorMatrixColorFilter(
                                            ColorMatrix()
                                        )
                                    } else {
                                        (checkedView as ImageView).colorFilter = ColorMatrixColorFilter(
                                            ColorMatrix()
                                        )
                                    }
                                    checkedView = null

                                }
                            }
                        }
                    }
                } else if (!startPromoting && yourSide == turn && (v as PieceView).figure.side == turn){
                    layout.findViewById<ViewMoves>(R.id.available_moves).also {
                        it.clean = true
                        it.invalidate()
                        if (v == checkedView){
                            (v as ImageView).colorFilter = ColorMatrixColorFilter(ColorMatrix())
                        } else {
                            (checkedView as ImageView).colorFilter = ColorMatrixColorFilter(
                                ColorMatrix()
                            )
                        }
                        checkedView = null

                    }
                }


            }

        }

        // Adding pawns
        for (i in 0..8){
            boardShogi[i+1,3] = Pawn(Side.WHITE,3,i+1,false,appInfo ,firstCellX + scaleX * (i) * delta,firstCellY + scaleY * 2 * delta,orientation,touchListener,false)
            boardShogi[i+1,7] = Pawn(Side.BLACK,7,i+1,false, appInfo,firstCellX + scaleX * (i) * delta,firstCellY + scaleY * 6 * delta,orientation,touchListener,false)
        }

        // Adding Silvers
        boardShogi[3,1] = Silver(Side.WHITE,1,3,false,appInfo,firstCellX + scaleX * 2 *delta,firstCellY+scaleY*0*delta,orientation,touchListener,false)
        boardShogi[7,1] = Silver(Side.WHITE,1,7,false,appInfo,firstCellX + scaleX * 6 *delta,firstCellY+scaleY*0*delta,orientation,touchListener,false)
        boardShogi[3,9] = Silver(Side.BLACK,9,3,false,appInfo,firstCellX + scaleX * 2 *delta,firstCellY+scaleY*8*delta,orientation,touchListener,false)
        boardShogi[7,9] = Silver(Side.BLACK,9,7,false,appInfo,firstCellX + scaleX * 6 *delta,firstCellY+scaleY*8*delta,orientation,touchListener,false)

        //Adding Golds
        boardShogi[4,1] = Gold(Side.WHITE,1,4,appInfo,firstCellX + scaleX * 3 *delta,firstCellY+scaleY*0*delta,orientation,touchListener,false)
        boardShogi[6,1] = Gold(Side.WHITE,1,6,appInfo,firstCellX + scaleX * 5 *delta,firstCellY+scaleY*0*delta,orientation,touchListener,false)
        boardShogi[4,9] = Gold(Side.BLACK,9,4,appInfo,firstCellX + scaleX * 3 *delta,firstCellY+scaleY*8*delta,orientation,touchListener,false)
        boardShogi[6,9] = Gold(Side.BLACK,9,6,appInfo,firstCellX + scaleX * 5 *delta,firstCellY+scaleY*8*delta,orientation,touchListener,false)

        //Adding Lances
        boardShogi[1,1] = Lance(Side.WHITE,1,1,false,appInfo,firstCellX + scaleX * 0 *delta,firstCellY+scaleY*0*delta,orientation,touchListener,false)
        boardShogi[9,1] = Lance(Side.WHITE,1,9,false,appInfo,firstCellX + scaleX * 8 *delta,firstCellY+scaleY*0*delta,orientation,touchListener,false)
        boardShogi[1,9] = Lance(Side.BLACK,9,1,false,appInfo,firstCellX + scaleX * 0 *delta,firstCellY+scaleY*8*delta,orientation,touchListener,false)
        boardShogi[9,9] = Lance(Side.BLACK,9,9,false,appInfo,firstCellX + scaleX * 8 *delta,firstCellY+scaleY*8*delta,orientation,touchListener,false)

        //Adding Kings
        boardShogi[5,1] = King(Side.WHITE,1,5,appInfo,firstCellX + scaleX * 4 *delta,firstCellY+scaleY*0*delta,orientation,touchListener,false)
        boardShogi[5,9] = King(Side.BLACK,9,5,appInfo,firstCellX + scaleX * 4 *delta,firstCellY+scaleY*8*delta,orientation,touchListener,false)

        //Adding Knights
        boardShogi[2,1] = Knight(Side.WHITE,1,2,false,appInfo,firstCellX + scaleX * 1 *delta,firstCellY+scaleY*0*delta,orientation,touchListener,false)
        boardShogi[8,1] = Knight(Side.WHITE,1,8,false,appInfo,firstCellX + scaleX * 7 *delta,firstCellY+scaleY*0*delta,orientation,touchListener,false)
        boardShogi[2,9] = Knight(Side.BLACK,9,2,false,appInfo,firstCellX + scaleX * 1 *delta,firstCellY+scaleY*8*delta,orientation,touchListener,false)
        boardShogi[8,9] = Knight(Side.BLACK,9,8,false,appInfo,firstCellX + scaleX * 7 *delta,firstCellY+scaleY*8*delta,orientation,touchListener,false)

        //Adding Rooks
        boardShogi[8,2] = Rook(Side.WHITE,2,8,false,appInfo,firstCellX + scaleX * 7 *delta,firstCellY+scaleY*1*delta,orientation,touchListener,false)
        boardShogi[2,8] = Rook(Side.BLACK,8,2,false,appInfo,firstCellX + scaleX * 1 *delta,firstCellY+scaleY*7*delta,orientation,touchListener,false)

        //Adding Bishops
        boardShogi[2,2] = Bishop(Side.WHITE,2,2,false,appInfo,firstCellX + scaleX * 1 *delta,firstCellY+scaleY*1*delta,orientation,touchListener,false)
        boardShogi[8,8] = Bishop(Side.BLACK,8,8,false,appInfo,firstCellX + scaleX * 7 *delta,firstCellY+scaleY*7*delta,orientation,touchListener,false)

    }

    fun createImageViews(context: Context, left:Int, bottom:Int, delta:Int, size:Int):ArrayList<ImageView> {
        val pieces:ArrayList<ImageView> = ArrayList();
        return pieces
    }


}
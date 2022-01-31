package ru.popov.shogi.classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import ru.popov.shogi.R
import ru.popov.shogi.classes.figures.Orientation
import java.util.ArrayList

class PieceBundleAdapter(var orientation: Orientation): RecyclerView.Adapter<PieceBundleAdapter.ViewHolder>() {

    private var piecesBundle = ArrayList<Integer>()
    private var bundlePieceValues = BundlePiece.values()
    fun addPiece(bundlePiece: BundlePiece){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view:View = LayoutInflater.from(parent.context).inflate(R.layout.cell,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (orientation == Orientation.NORMAL) {
            holder.pieceView.setImageResource(bundlePieceValues[position].normalId)
        } else {
            holder.pieceView.setImageResource(bundlePieceValues[position].reverseId)
        }
    }

    override fun getItemCount(): Int {
        return 7
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var pieceView: ImageView = itemView.findViewById(R.id.piece)
    }
}
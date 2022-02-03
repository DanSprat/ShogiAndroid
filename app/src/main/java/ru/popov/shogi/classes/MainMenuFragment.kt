package ru.popov.shogi.classes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import ru.popov.shogi.R

class MainMenuFragment(): Fragment() {
    private var btnOneDevice: Button? = null
    private var btnOnlineGame: Button? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.main_menu, container, false)
        btnOneDevice = view.findViewById(R.id.one_device)

        btnOneDevice?.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().also {
                it.replace(R.id.layout,ShogiFragment())
                it.commit()
            }
        }


        btnOnlineGame = view.findViewById(R.id.online_game)
        btnOnlineGame?.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().also {
                it.replace(R.id.layout,MultiplayerShogiFragment())
                it.commit()
            }
        }
        return view
    }
}
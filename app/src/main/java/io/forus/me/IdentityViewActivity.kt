package io.forus.me

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import io.forus.me.helpers.ThreadHelper
import io.forus.me.services.IdentityService
import io.forus.me.services.Web3Service
import io.forus.me.views.me.IdentityViewAdapter
import kotlinx.android.synthetic.main.activity_identity_view.*
import java.util.concurrent.Callable

class IdentityViewActivity : AppCompatActivity() {

    private var tmp: Int = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_identity_view)
        setSupportActionBar(toolbar)

        add_button.setOnClickListener { view ->
            --tmp
            if (tmp > 0) {
                Toast.makeText(this, "Nog $tmp keer drukken om uit te loggen", Toast.LENGTH_SHORT).show()
            } else {
                Web3Service.deleteAccount()
                val restart = Intent(this, MainActivity::class.java)
                restart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(restart)
            }

            /*
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()*/
        }
        val identityData = ThreadHelper.await(Callable{
            IdentityService.getIdentities()
        })
        val list:RecyclerView = this.findViewById(R.id.identity_list)
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = IdentityViewAdapter(this, identityData!!)
    }
}

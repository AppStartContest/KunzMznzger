package com.ltei.kunzmznzger.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import com.ltei.kunzmznzger.MainActivity
import com.ltei.kunzmznzger.R
import com.ltei.kunzmznzger.libs.Helpers
import com.ltei.kunzmznzger.local.LocalUserInfo
import kotlinx.android.synthetic.main.activity_user_creation.*

class UserCreationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_creation)

        button_confirm.setOnClickListener { submit() }
        edittext_confirm_password.setOnEditorActionListener { v: TextView?, actionId: Int, event: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                submit()
                true
            } else {
                false
            }
        }
    }

    fun submit() {
        val name = edittext_name.text.toString()
        if (name == "") {
            Toast.makeText(this@UserCreationActivity, "You have to enter your name!", Toast.LENGTH_SHORT).show()
        } else {
            val pseudo = edittext_pseudo.text.toString()
            if (pseudo == "") {
                Toast.makeText(this@UserCreationActivity, "You have to enter your pseudo!", Toast.LENGTH_SHORT).show()
            } else {
                val password = edittext_password.text.toString()
                if (password.length < 6) {
                    Toast.makeText(this@UserCreationActivity, "Your password must be at least 6 characters long", Toast.LENGTH_SHORT).show()
                } else {
                    val confirmPassword = edittext_confirm_password.text.toString()
                    if (confirmPassword != password) {
                        Toast.makeText(this@UserCreationActivity, "Passwords don't correspond", Toast.LENGTH_SHORT).show()
                    } else {
                        LocalUserInfo.getInstance().create(this, pseudo, name, password,
                                Runnable {
                                    Helpers.log("resolved")
                                    runOnUiThread {
                                        val intent = Intent(this, MainActivity::class.java)
                                        startActivity(intent)
                                        this@UserCreationActivity.finish()
                                    }
                                },
                                Runnable {
                                    Helpers.log("rejected")
                                    runOnUiThread {
                                        Toast.makeText(this@UserCreationActivity, "This username has already been taken.", Toast.LENGTH_SHORT).show()
                                    }
                                })
                    }
                }
            }
        }
    }

}
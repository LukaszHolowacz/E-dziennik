package com.example.e_dziennik


import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val login_text = findViewById<EditText>(R.id.login_text)
        val haslo_text = findViewById<EditText>(R.id.haslo_text)
        val login_button = findViewById<Button>(R.id.login_button)

        login_button.setOnClickListener {
            val resID = resources.getIdentifier("dane_uzytkownika", "raw", packageName)
            val inputStream = resources.openRawResource(resID)
            val reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
            val stringBuilder = StringBuilder()
            var line = reader.readLine()
            while (line != null) {
                stringBuilder.append(line)
                line = reader.readLine()
            }
            val jsonString = stringBuilder.toString()
            val json = JSONObject(jsonString)
            for (i in 0 until json.length())
            {
                val uzytkownik = json.getJSONObject(i.toString())
                val login = uzytkownik.getString("login")
                val haslo = uzytkownik.getString("haslo")

                if(login_text.text.toString() == login && haslo_text.text.toString() == haslo){
                    val intent2 = Intent(this, WidokOcenActivity::class.java)
                    intent2.putExtra("user_id", i.toString())
                    intent2.putExtra("isLogged", true)
                    startActivity(intent2)
                    finish()
                }
                else{
                    login_text.setText("")
                    haslo_text.setText("")
                    Toast.makeText(applicationContext, "Zły login lub hasło!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun showMenu(view: View){
        val popup = PopupMenu(this, view)
        popup.inflate(R.menu.menu_logged_out)
        popup.show()
    }

    fun onMenuItemClick(item: MenuItem?) {
        when(item?.itemId){
            R.id.logowanie -> {
                swapActivity(LoginActivity::class.java)
            }
            R.id.informacje -> {
                swapActivity(InformationAboutUserActivity::class.java)
            }
            R.id.oceny -> {
                swapActivity(WidokOcenActivity::class.java)
            }
            else-> Toast.makeText(applicationContext, "Błąd, Nie ma takiej opcji w menu!", Toast.LENGTH_SHORT).show()
        }
    }

    fun swapActivity(activityClass: Class<out Activity>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
        finish()
    }
}
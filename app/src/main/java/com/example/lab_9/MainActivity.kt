package com.example.lab_9

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    private lateinit var editText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editText = findViewById(R.id.editText)

        //val button: Button = findViewById(R.id.testBtton)

        val exitButton: Button = findViewById(R.id.exit_button)

        registerForContextMenu(editText)

        /*button.setOnClickListener {
            // Обработка нажатия на кнопку
            val text = editText.text.toString()
            if (text.isEmpty()) {
                showAlertDialog("Ошибка!", "Строка не должна быть пустой!")
            } else if (text.any{it.isDigit()}) {
                showAlertDialog("Ошибка", "Строка не должна содержать цифры")
            } else {
                showAlertDialog("Вывод строки", "Текст: $text")
            }
        }*/

        exitButton.setOnClickListener {
            showExitDialog();
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_clear -> {
                editText.text.clear()
                return true
            }
            R.id.show_text -> {
                if (item.isChecked) {
                    editText.visibility = View.VISIBLE
                    item.isChecked = false
                } else {
                    editText.visibility = View.INVISIBLE
                    item.isChecked = true
                }
                return true
            }
            R.id.action_about -> {
                showInfoDialog()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val editText = findViewById<EditText>(R.id.editText)

        when (item.itemId) {
            R.id.action_select_all -> {
                editText.selectAll()
                return true
            }
            R.id.action_cut -> {
                // Вырезать выделенный текст
                editText.text?.let {
                    val start = editText.selectionStart
                    val end = editText.selectionEnd
                    val selectedText = it.substring(start, end)
                    it.replace(start, end, "")
                    // Копировать вырезанный текст в буфер обмена (необязательно)
                    val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboardManager.setPrimaryClip(ClipData.newPlainText("cut_text", selectedText))
                }
                return true
            }
            R.id.action_paste -> {
                // Вставить текст из буфера обмена
                val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = clipboardManager.primaryClip
                if (clipData != null && clipData.itemCount > 0) {
                    val pasteText = clipData.getItemAt(0).text.toString()
                    val start = editText.selectionStart
                    editText.text?.insert(start, pasteText)
                }
                return true
            }
            else -> return super.onContextItemSelected(item)
        }
    }
    private fun showAlertDialog(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showExitDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Вы действительно хотите выйти?")
        builder.setCancelable(false)
        builder.setPositiveButton("Да") { _, _ ->
            finish()
        }
        builder.setNegativeButton("Нет") { dialog, _ ->
            dialog.cancel()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun showInfoDialog() {
        val builder = AlertDialog.Builder(this)
        try {
            val appInfo = packageManager.getPackageInfo(packageName, 0)
            val message = "${title} версия ${appInfo.versionName}\n\nАвтор - ${R.string.fio}"
            builder.setMessage(message)
        } catch (e: PackageManager.NameNotFoundException) {
            print("lox ebaniy")
        }
        builder.setTitle("О программе")
        builder.setNeutralButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        builder.setIcon(R.mipmap.ic_launcher_round)
        val alertDialog = builder.create()
        alertDialog.show()

    }
}

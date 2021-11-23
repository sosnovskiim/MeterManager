package com.sosnowskydevelop.metermanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        var name: String = ""
//        var description: String?
//        if (requestCode == locationDetailsActivityRequestCode && resultCode == Activity.RESULT_OK) {
//            data?.getStringExtra("LOCATION_NAME")?.let {
//                name = it
//            }
//            description = data?.getStringExtra("LOCATION_DESCRIPTION")
//            val location = Location(0, name, description)
//            locationViewModel.insert(location)
//        } else {
//            Toast.makeText(
//                applicationContext,
//                R.string.empty_not_saved,
//                Toast.LENGTH_LONG).show()
//        }
//    }
}
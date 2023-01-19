package kr.co.vilez.ui

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import kr.co.vilez.R
import kr.co.vilez.databinding.ActivityMainBinding
import kr.co.vilez.ui.chat.ChatFragment
import kr.co.vilez.ui.share.ShareFragment
import kr.co.vilez.ui.user.ProfileFragment
import kr.co.vilez.util.StompClient
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        supportActionBar?.hide() // 액션바 숨김
        println(getDebugHashKey())

        StompClient.runStomp();
        initView()
    }


    private fun getDebugHashKey() {
        var packageInfo: PackageInfo? = null
        try {
            packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            if (packageInfo == null) Log.e("KeyHash", "KeyHash:null") else {
                for (signature in packageInfo.signatures) {
                    val md: MessageDigest = MessageDigest.getInstance("SHA")
                    md.update(signature.toByteArray())
                    Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT))
                }
            }
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }
    private fun initView() {
        // 가장 첫 화면은 홈 화면의 Fragment로 지정
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout_main, HomeFragment())
            .commit()

        binding.bottomNavigation.itemIconTintList = null // 클릭해도 아이콘 테마색으로 변경되는거 막기
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.page_home -> {
                    // 아이콘 변경
                    item.setIcon(R.drawable.home_fill)
                    binding.bottomNavigation.menu.findItem(R.id.page_share).setIcon(R.drawable.location_line)
                    binding.bottomNavigation.menu.findItem(R.id.page_chat).setIcon(R.drawable.message_line)
                    binding.bottomNavigation.menu.findItem(R.id.page_profile).setIcon(R.drawable.user_line)
                    // Fragment 변경
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_main, HomeFragment())
                        .commit()
                    true
                }
                R.id.page_share -> {
                    // 아이콘 변경
                    item.setIcon(R.drawable.location_fill)
                    binding.bottomNavigation.menu.findItem(R.id.page_home).setIcon(R.drawable.home_line)
                    binding.bottomNavigation.menu.findItem(R.id.page_chat).setIcon(R.drawable.message_line)
                    binding.bottomNavigation.menu.findItem(R.id.page_profile).setIcon(R.drawable.user_line)

                    // Fragment 변경
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_main, ShareFragment())
                        .commit()
                    true
                }
                R.id.page_chat -> {
                    // 아이콘 변경
                    item.setIcon(R.drawable.message_fill)
                    binding.bottomNavigation.menu.findItem(R.id.page_home).setIcon(R.drawable.home_line)
                    binding.bottomNavigation.menu.findItem(R.id.page_share).setIcon(R.drawable.location_line)
                    binding.bottomNavigation.menu.findItem(R.id.page_profile).setIcon(R.drawable.user_line)

                    // Fragment 변경
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_main, ChatFragment())
                        .commit()
                    true
                }
                R.id.page_profile -> {
                    // 아이콘 변경
                    item.setIcon(R.drawable.user_fill)
                    binding.bottomNavigation.menu.findItem(R.id.page_home).setIcon(R.drawable.home_line)
                    binding.bottomNavigation.menu.findItem(R.id.page_share).setIcon(R.drawable.location_line)
                    binding.bottomNavigation.menu.findItem(R.id.page_chat).setIcon(R.drawable.message_line)

                    // Fragment 변경
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_main, ProfileFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }

    }
}
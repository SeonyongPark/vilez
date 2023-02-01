package kr.co.vilez.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.vilez.R
import kr.co.vilez.databinding.FragmentHomeBinding
import kr.co.vilez.ui.share.*
import kr.co.vilez.ui.share.category.MenuCategoryActivity
import kr.co.vilez.ui.share.search.ShareSearchActivity
import kr.co.vilez.util.ApplicationClass
import kr.co.vilez.util.Common
import kr.co.vilez.util.Common.Companion.elapsedTime
import retrofit2.awaitResponse
import kr.co.vilez.ui.share.ShareDetailActivity

private const val TAG = "빌리지_HomeFragment"
class HomeFragment : Fragment() {
    private lateinit var binding:FragmentHomeBinding
    private lateinit var mainActivity: MainActivity

    private lateinit var shareAdapter: ShareListAdapter
    private lateinit var shareDatas: ArrayList<ShareData>
    private var index = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = context as MainActivity
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.fragment = this

        initToolBar()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    fun initList() {
        // 데이터 가져오기
        shareDatas = arrayListOf()

        // 어댑터 생성
        shareAdapter = ShareListAdapter(shareDatas)
        shareAdapter.setItemClickListener(object : ShareListAdapter.OnItemClickListener {
            // listview item 클릭시 실행할 메소드
            override fun onClick(view: View, position: Int) {

                Log.d(TAG, "onClick: ${shareDatas[position].tv_name} clicked!")
            }
        })

        // 리사이클러뷰에 어댑터 등록
        binding.rvShareList.apply {
            adapter = shareAdapter
            layoutManager = LinearLayoutManager(mainActivity, LinearLayoutManager.VERTICAL, false)
        }

        var num = 0;
        var max = 10;
        CoroutineScope(Dispatchers.Main).launch {
            val result =
                ApplicationClass.retrofitShareService.boardList(num++, max).awaitResponse().body();

            if(result?.flag == "success") {
                Log.d(TAG, "initList: result: $result")
                if(result.data.isEmpty()) {
                    Log.d(TAG, "onViewCreated: 데이터 0개")
                    binding.tvNoArticleMsg.visibility = View.VISIBLE
                }
                for(data in result.data){
                    var shareData:ShareData
                    if(data.shareListDto.list.isEmpty()){

                        shareData = ShareData(
                            data.shareListDto.id,
                            "https://kr.object.ncloudstorage.com/vilez/basicProfile.png",
                            data.shareListDto.title,
                            Common.elapsedTime(data.shareListDto.date),
                            "구미",
                            data.shareListDto.startDay+"~"
                                    +data.shareListDto.endDay,
                            Integer.toString(data.listCnt)
                        );
                    } else {
                        shareData = ShareData(
                            data.shareListDto.id,
                            data.shareListDto.list[0].path,
                            data.shareListDto.title,
                            Common.elapsedTime(data.shareListDto.date),
                            "구미",
                            data.shareListDto.startDay+"~"
                                    +data.shareListDto.endDay,
                            Integer.toString(data.listCnt)
                        );
                    }
                    shareDatas.add(shareData)
//                    Log.d(TAG, "initView: ${data.shareListDto.nickName}");
//                    Log.d(TAG, "initView: ${data.shareListDto.list[0].path}");
                }
            }
            shareAdapter.notifyItemInserted(index-1)
        }

        val items = resources.getStringArray(R.array.my_array)
        //val myAdapter = ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item,items)
        val arrayAdapter = ArrayAdapter<String>(
            mainActivity,
            android.R.layout.simple_spinner_dropdown_item,
            items
        )

       /* binding.spinner.adapter = arrayAdapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                binding.textView.setText(items.get(position))
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.textView.setText("선택")
            }
        }*/

        binding.rvShareList.setOnScrollChangeListener{ v, scollX, scrollY,
                                              oldScrollX, oldScrollY ->
            if(!v.canScrollVertically(1)){
                CoroutineScope(Dispatchers.Main).launch {
                    val result =
                        ApplicationClass.retrofitShareService.boardList(num++, max).awaitResponse()
                            .body();
                    Log.d(TAG, "initView: ${result?.data}")
                    if (result?.data?.size != 0) {
                        if (result?.flag == "success") {
                            for (data in result.data) {
                                var shareData:ShareData
                                if(data.shareListDto.list.size == 0){
                                    shareData = ShareData(
                                        data.shareListDto.id,
                                        "https://kr.object.ncloudstorage.com/vilez/basicProfile.png",
                                        data.shareListDto.title,
                                        elapsedTime(data.shareListDto.date),
                                        "구미",
                                        data.shareListDto.startDay+"~"
                                                +data.shareListDto.endDay,
                                        Integer.toString(data.listCnt)
                                    );
                                } else {
                                    shareData = ShareData(
                                        data.shareListDto.id,
                                        data.shareListDto.list[0].path,
                                        data.shareListDto.title,
                                        elapsedTime(data.shareListDto.date),
                                        "구미",
                                        data.shareListDto.startDay+"~"
                                                +data.shareListDto.endDay,
                                        Integer.toString(data.listCnt)
                                    );
                                }
                                shareDatas.add(shareData)
                            }
                        }
                        shareAdapter.notifyItemInserted(index - 1)
                    }
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_app_bar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.option_menu_menu -> {
                Log.d(TAG, "onContextItemSelected: ${item.title} clicked")
                Toast.makeText(mainActivity, "${item.title} 클릭", Toast.LENGTH_SHORT).show()
                startActivity(Intent(mainActivity, MenuCategoryActivity::class.java))
            }
            R.id.option_menu_search -> {
                val intent = Intent(mainActivity, ShareSearchActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun initToolBar() {
        mainActivity.setSupportActionBar(binding.toolbar)
        mainActivity.supportActionBar?.setDisplayShowTitleEnabled(false) // 기본 타이틀 제거
        binding.title = "진평동"
    }

    companion object {

    }

    fun moveToShareActivity(view: View) {
        val intent = Intent(mainActivity, ShareDetailActivity::class.java)
        mainActivity.startActivity(intent)
    }

    fun moveToShareWriteActivity(view: View) {
        val intent = Intent(mainActivity, ShareWriteActivity::class.java)
        mainActivity.startActivity(intent)
    }
}
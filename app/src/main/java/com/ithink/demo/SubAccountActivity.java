package com.ithink.demo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.ithink.bean.SubAccountInfoBean;
import com.ithink.bean.IthinkBean;
import com.ithink.camera.control.ITHKDeviceManager;
import com.ithink.camera.control.ITHKStatus;
import com.ithink.camera.control.ITHKStatusListener;
import com.ithink.view.MyProgressDialog;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 子账号管理
 * @author Vine
 *
 */
public class SubAccountActivity extends Activity implements OnClickListener{

	private final String TAG = SubAccountActivity.class.getSimpleName();

	private Context context;

	private ImageView title_left_img;

	private TextView title_center_txt;
	private TextView title_left_txt;
	private TextView title_right_txt;

	private View title_left_ll;
	
	private TextView tip_tv;
	private ListView list_view;
 	
	private ITHKDeviceManager ithkDeviceManager;
	
	private String sid;
	
	private List<SubAccountInfoBean> userInfoBeanList;
	
	private MyProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		context = this;
		
		setContentView(R.layout.activity_sub_account);
		
		Bundle bundle = getIntent().getExtras();
		sid = bundle.getString("sid");
		
		ithkDeviceManager = new ITHKDeviceManager(context);
		
		initViews();
		
		initEvents();
		
		showProgressDialog();
		
		ithkDeviceManager.getSubAccountListForSid(sid, 1, 100);
		
	}
	
	private void initViews(){
		title_left_ll = (View) findViewById(R.id.title_left_ll);
		title_left_img = (ImageView) findViewById(R.id.title_left_img);
		title_center_txt = (TextView) findViewById(R.id.title_center_txt);
		title_left_txt = (TextView) findViewById(R.id.title_left_txt);
		title_right_txt = (TextView) findViewById(R.id.title_right_txt);
		title_left_img.setImageResource(R.drawable.selector_title_back);
		title_center_txt.setText("子账号管理");
		title_left_txt.setText(R.string.back);
		title_right_txt.setText("添加");
		
		
		tip_tv = (TextView) findViewById(R.id.tip_tv);
		list_view = (ListView) findViewById(R.id.userListView);
	}

	private void initEvents(){
		title_left_ll.setOnClickListener(this);
		title_right_txt.setOnClickListener(this);
		ithkDeviceManager.setGetSubAccountListListener(new ITHKStatusListener() {
			
			@Override
			public void ithkStatus(int status) {
				// TODO Auto-generated method stub
				if (progressDialog!=null)
					progressDialog.dismiss();
				
				if (status == ITHKStatus.kIS_OK){
					
					IthinkBean ithinkBean = JSON.parseObject(ithkDeviceManager.subAccountJsonChar, IthinkBean.class);
					userInfoBeanList  = ithinkBean.getUserList();
					if (userInfoBeanList!=null && userInfoBeanList.size() >1){
						list_view.setVisibility(View.VISIBLE);
						tip_tv.setVisibility(View.GONE);
						getData();
					}else{
						list_view.setVisibility(View.GONE);
						tip_tv.setVisibility(View.VISIBLE);
						tip_tv.setText("没有关联的子账号");
					}
				}else{
					if (status == ITHKStatus.kIS_ErrorCode){
						Toast.makeText(context, "获取子账号 -> 合作厂商代码错误", Toast.LENGTH_SHORT).show();
					}else if (status == ITHKStatus.kIS_Error){
						Toast.makeText(context, "获取子账号 -> 提交参数信息错误", Toast.LENGTH_SHORT).show();
					}else if (status == ITHKStatus.kIS_Timeout){
						Toast.makeText(context, "获取子账号 -> 网络超时", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		
		ithkDeviceManager.setAddSubAccountListListener(new ITHKStatusListener() {
			
			@Override
			public void ithkStatus(int status) {
				// TODO Auto-generated method stub
				if (progressDialog!=null)
					progressDialog.dismiss();
				
				if (status == ITHKStatus.kIS_OK){
					ithkDeviceManager.getSubAccountListForSid(sid, 1, 100);
					Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
				}else{
					if (status == ITHKStatus.kIS_ErrorCode){
						Toast.makeText(context, "添加子账号 -> 合作厂商代码错误", Toast.LENGTH_SHORT).show();
					} else if (status == ITHKStatus.kIS_Error){
						Toast.makeText(context, "添加子账号 -> 提交参数信息错误", Toast.LENGTH_SHORT).show();
					} else if (status == ITHKStatus.kIS_NoUsr){
						Toast.makeText(context, "添加子账号 -> 用户不存在", Toast.LENGTH_SHORT).show();
					} else if (status == ITHKStatus.kIS_OutMax){
						Toast.makeText(context, "添加子账号 -> 超过最大添加数", Toast.LENGTH_SHORT).show();
					} else if (status == ITHKStatus.kIS_Repeat){
						Toast.makeText(context, "添加子账号 -> 重复添加", Toast.LENGTH_SHORT).show();
					} else if (status == ITHKStatus.kIS_Timeout){
						Toast.makeText(context, "添加子账号 -> 网络超时", Toast.LENGTH_SHORT).show();
					} 
				}
			}
		});
		
		ithkDeviceManager.setdeleteSubAccountListListener(new ITHKStatusListener() {
			
			@Override
			public void ithkStatus(int status) {
				// TODO Auto-generated method stub
				if (progressDialog!=null)
					progressDialog.dismiss();
				
				if (status == ITHKStatus.kIS_OK){
					ithkDeviceManager.getSubAccountListForSid(sid, 1, 100);
					Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
				}else{
					if (status == ITHKStatus.kIS_ErrorCode){
						Toast.makeText(context, "添加子账号 -> 合作厂商代码错误", Toast.LENGTH_SHORT).show();
					} else if (status == ITHKStatus.kIS_Error){
						Toast.makeText(context, "添加子账号 -> 提交参数信息错误", Toast.LENGTH_SHORT).show();
					}else if (status == ITHKStatus.kIS_Timeout){
						Toast.makeText(context, "添加子账号 -> 网络超时", Toast.LENGTH_SHORT).show();
					} 
				}
			}
		});
	}

	private List<String> text;
	private UserListAdapter adapter;
	private void getData() {

		text = new ArrayList<String>();
		// userInfoBeanList.get(i).getNickName() +
		if (userInfoBeanList!=null){
			for (int i = 0; i < userInfoBeanList.size(); i++) {
				String str = (userInfoBeanList.get(i).getUid());
	
				if (userInfoBeanList.get(i).getIsAdmin() == 1) {
					
				} else {
					text.add(str);
				}
	
			}
		}
		
		// 初始化数据结束
		adapter = new UserListAdapter(context, text);

		list_view.setAdapter(adapter);
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == title_left_ll){
			finish();
		}
		
		if (v == title_right_txt){
			addDevUserDialog();
		}
	}
	
	private EditText edtName;

	private void addDevUserDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		edtName = new EditText(context);

		edtName.setSingleLine();
		edtName.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
		edtName.setHint("请输入用户名");
		// edtName.setInputType(InputType.TYPE_CLASS_TEXT
		// | InputType.TYPE_TEXT_VARIATION_PASSWORD);

		builder.setMessage("")
				.setView(edtName)
				.setPositiveButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						try {
							Field field = dialog.getClass().getSuperclass()
									.getDeclaredField("mShowing");
							field.setAccessible(true);
							// 设置mShowing值，欺骗android系统
							field.set(dialog, true);
						} catch (Exception e) {
							e.printStackTrace();
						}
						dialog.cancel();

					}
				})
				.setNegativeButton("添加", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						String newUsername = edtName.getText().toString();
						if (newUsername.equals("")) {
							try {
								Field field = dialog.getClass().getSuperclass()
										.getDeclaredField("mShowing");
								field.setAccessible(true);
								// 设置mShowing值，欺骗android系统
								field.set(dialog, false);
							} catch (Exception e) {
								e.printStackTrace();
							}
							Toast.makeText(context, "请输入用户名",
									Toast.LENGTH_SHORT).show();

						} else {
							try {
								Field field = dialog.getClass().getSuperclass()
										.getDeclaredField("mShowing");
								field.setAccessible(true);
								// 设置mShowing值，欺骗android系统
								field.set(dialog, true);
							} catch (Exception e) {
								e.printStackTrace();
							}

							showProgressDialog();
							
							ithkDeviceManager.addSubAccountForSid(newUsername, sid);
						}
					}
				}).setCancelable(true).show();

	}
	
	private void showProgressDialog() {
		progressDialog = new MyProgressDialog(context);
		progressDialog.show();
		progressDialog.setProgressBarGone(View.VISIBLE);
		progressDialog.changeDialogTitle("");
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setCancelable(true);
	}
	
	public class UserListAdapter extends BaseAdapter {

		private ProgressDialog progressDialog;
		private Context context;
		private List<String> text;
		private String strUid;
		private int delCount;

		public UserListAdapter(Context context, List<String> text) {
			this.context = context;
			this.text = text;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return text.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return text.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final int index = position;
			View view = convertView;
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.row_simple_list_item, null);
			}
			final TextView textView = (TextView) view
					.findViewById(R.id.simple_item_1);
			textView.setText(text.get(position));

			final ImageView imageView = (ImageView) view
					.findViewById(R.id.simple_item_2);
			imageView.setVisibility(View.VISIBLE);
			textView.setVisibility(View.VISIBLE);
			imageView.setBackgroundResource(android.R.drawable.ic_delete);
			imageView.setTag(position);
			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					askForOut(index, textView.getText().toString());
				}
			});
			return view;
		}

		private void askForOut(final int index, final String str) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);

			builder.setMessage("删除子账号  "+str + "?")
					.setNegativeButton("删除", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							String str = text.get(index);
							int n = str.indexOf("(");
							if (n != -1){
								strUid = str.substring(n + 1, str.length() - 1);
							}else{
								strUid = str;
							}
							
							showProgressDialog();
							
							ithkDeviceManager.deleteSubAccountForSid(strUid, sid);

							delCount = index;
						}
					})
					.setPositiveButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					}).setCancelable(false).show();
		}

	}
}

package weixinapi;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * @author  ChenShaoWen
 * 2017年4月15日  下午9:05:06
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

	private IWXAPI api;
	/**
	 * 微信api接口的初始化
	 */
	//这里的key是对应申请的apiid
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
	
		api =WXAPIFactory.createWXAPI(this, "wx10c3e7c3a47dd0cb", false);
		api.handleIntent(getIntent(), this);
		super.onCreate(savedInstanceState);
	}
	@Override
	public void onReq(BaseReq arg0) {
	}
	@Override
	public void onResp(BaseResp resp) {
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			Toast.makeText(this, "即将分享至微信", 0).show();
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			Toast.makeText(this, "取消分享", 0).show();
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			Toast.makeText(this, "分享拒绝", 0).show();
			break;
		}
		
	}
}

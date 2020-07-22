package com.shopping.adapter;

//∂‡Õº∆¨‰Ø¿¿

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.shopping.activity.R;
import com.shopping.util.ImageLoader;


public class PicDetailsGalleryAdapter extends BaseAdapter{
	private Context context;
	private LayoutInflater inflater;
	private String[] pics_image;
	public PicDetailsGalleryAdapter(Context context) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
	}



	public String[] getPics_image() {
		return pics_image;
	}


	public void setPics_image(String[] pics_image) {
		this.pics_image = pics_image;
	}


	@Override
	public int getCount() {
		return pics_image == null ? 0 : pics_image.length;
	}

	@Override
	public Object getItem(int position) {
		return 0;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	
	
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (null == convertView) {
			convertView = inflater.inflate(R.layout.gallery_pics_item, null);
			holder = new ViewHolder();
			holder.imagePic = (ImageView) convertView.findViewById(R.id.imagePic);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ImageLoader.getInstance().asyncLoadBitmap(pics_image[position], new ImageLoader.ImageCallback() {
			@Override
			public void imageLoaded(Bitmap bmp, String url) {
				if(bmp!=null){
					holder.imagePic.setBackgroundDrawable(new BitmapDrawable(bmp));
				}else{
					holder.imagePic.setBackgroundResource(R.drawable.category_icon_123);
				}
			}
		});
		
		return convertView;
	}
	class ViewHolder {
		ImageView imagePic;
	}
}

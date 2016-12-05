package com.example.abc.fragments.inputcells;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.example.abc.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PictureInputCellFragment extends BaseInputCellFragment {

	final int REQUESTCODE_CAMERA = 1;
	final int REQUESTCODE_ALBUM = 2;
	
	ImageView imageView;
	TextView labelText;
	TextView hintText;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_inputcell_picture, container);
		imageView = (ImageView) view.findViewById(R.id.image);
		labelText = (TextView) view.findViewById(R.id.label);
		hintText = (TextView) view.findViewById(R.id.hint);

		imageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				OnImageViewClicked();
			}
		});
		return view;
	}

	void OnImageViewClicked() {
		String[] items = { "≈ƒ’’", "œ‡≤·" };
		new AlertDialog.Builder(getActivity()).setTitle(labelText.getText())
				.setItems(items, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						switch (which) {
						case 0:
							takePhoto();
							break;

						case 1:
							pickFrmCamera();
							break;
						default:
							break;
						}
					}

					
				})
				.setNegativeButton("»°œ˚", null)
				.show();

	}

	void pickFrmCamera() {
		Intent itnt = new Intent(Intent.ACTION_GET_CONTENT);
		itnt.setType("image/*");
		startActivityForResult(itnt, REQUESTCODE_ALBUM);
	}

	void takePhoto() {
		Intent itnt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(itnt, REQUESTCODE_CAMERA);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == Activity.RESULT_CANCELED) return;
		
		if(requestCode == REQUESTCODE_CAMERA) {
//			Log.d("camera capture", data.getDataString());
//			Toast.makeText(getActivity(), "aaa", Toast.LENGTH_SHORT);
			Bitmap bmp = (Bitmap) data.getExtras().get("data");
			imageView.setImageBitmap(bmp);
		} else if (requestCode == REQUESTCODE_ALBUM) {
//			Uri dataUri = data.getData();
//			Toast.makeText(getActivity(), dataUri.toString(), Toast.LENGTH_LONG).show();
			try {
				Bitmap bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
				imageView.setImageBitmap(bmp);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void setHintText(String hintText) {
		// TODO Auto-generated method stub
		this.hintText.setHint(hintText);
	}

	@Override
	public void setLabelText(String labelText) {
		// TODO Auto-generated method stub
		this.labelText.setText(labelText);
	}

}

/*******************************************************************************
 * Copyright 2014 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.jasper.myandroidtest.library.uil.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.jasper.myandroidtest.R;
import com.jasper.myandroidtest.library.uil.Constants;
import com.jasper.myandroidtest.library.uil.fragment.ImageGalleryFragment;
import com.jasper.myandroidtest.library.uil.fragment.ImageGridFragment;
import com.jasper.myandroidtest.library.uil.fragment.ImageListFragment;
import com.jasper.myandroidtest.library.uil.fragment.ImageListV3Fragment;
import com.jasper.myandroidtest.library.uil.fragment.ImagePagerFragment;
import com.jasper.myandroidtest.library.uil.fragment.ImageListV2Fragment;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class SimpleImageActivity extends FragmentActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int frIndex = getIntent().getIntExtra(Constants.Extra.FRAGMENT_INDEX, 0);
		Fragment fr;
		String tag;
		int titleRes;
		switch (frIndex) {
			default:
			case ImageListV2Fragment.INDEX:
				tag = ImageListV2Fragment.class.getSimpleName();
				fr = getSupportFragmentManager().findFragmentByTag(tag);
				if (fr == null) {
					fr = new ImageListV2Fragment();
				}
				titleRes = R.string.ac_name_image_list;
				break;
			case ImageListV3Fragment.INDEX:
				tag = ImageListV3Fragment.class.getSimpleName();
				fr = getSupportFragmentManager().findFragmentByTag(tag);
				if (fr == null) {
					fr = new ImageListV3Fragment();
				}
				titleRes = R.string.ac_name_image_list;
				break;
			case ImageListFragment.INDEX:
				tag = ImageListFragment.class.getSimpleName();
				fr = getSupportFragmentManager().findFragmentByTag(tag);
				if (fr == null) {
					fr = new ImageListFragment();
				}
				titleRes = R.string.ac_name_image_list;
				break;
			case ImageGridFragment.INDEX:
				tag = ImageGridFragment.class.getSimpleName();
				fr = getSupportFragmentManager().findFragmentByTag(tag);
				if (fr == null) {
					fr = new ImageGridFragment();
				}
				titleRes = R.string.ac_name_image_grid;
				break;
			case ImagePagerFragment.INDEX:
				tag = ImagePagerFragment.class.getSimpleName();
				fr = getSupportFragmentManager().findFragmentByTag(tag);
				if (fr == null) {
					fr = new ImagePagerFragment();
					fr.setArguments(getIntent().getExtras());
				}
				titleRes = R.string.ac_name_image_pager;
				break;
			case ImageGalleryFragment.INDEX:
				tag = ImageGalleryFragment.class.getSimpleName();
				fr = getSupportFragmentManager().findFragmentByTag(tag);
				if (fr == null) {
					fr = new ImageGalleryFragment();
				}
				titleRes = R.string.ac_name_image_gallery;
				break;
		}

		setTitle(titleRes);
		getSupportFragmentManager().beginTransaction().replace(android.R.id.content, fr, tag).commit();
	}
}
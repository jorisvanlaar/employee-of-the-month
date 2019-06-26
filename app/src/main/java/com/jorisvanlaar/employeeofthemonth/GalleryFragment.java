package com.jorisvanlaar.employeeofthemonth;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends Fragment {

    private int mColumnCount = 3;

    public GalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.galleryRecyclerView);
        File galleryFolder = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/Image Gallery");

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int imageWidth = displayMetrics.widthPixels / mColumnCount;
        int imageHeight = imageWidth * 4 / 3;

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), mColumnCount);
        recyclerView.setLayoutManager(layoutManager);

        File[] sortedGalleryFolder = sortFilesToLatest(galleryFolder);
        RecyclerView.Adapter imageAdapter = new ImageAdapter(sortedGalleryFolder, imageWidth, imageHeight);
        recyclerView.setAdapter(imageAdapter);

        return rootView;
    }

    private File[] sortFilesToLatest(File imageDirectory) {
        File[] files = imageDirectory.listFiles();
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return Long.valueOf(o2.lastModified()).compareTo(o1.lastModified());
            }
        });
        return files;
    }
}

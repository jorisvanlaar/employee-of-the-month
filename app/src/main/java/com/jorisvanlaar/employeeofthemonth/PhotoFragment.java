package com.jorisvanlaar.employeeofthemonth;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoFragment extends Fragment {

    private static final int CAMERA_REQUEST_CODE = 0;
    private static final int GALLERY_REQUEST_CODE = 1;
    private ImageView mCapturedImageView;
    private String mCacheFileLocation;
    private File mCacheFolder;
    private File mGalleryFolder;
    private TextView mMonthText;
    private FrameLayout mStickerFrameLayout;

    public PhotoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_photo, container, false);

        mMonthText = rootView.findViewById(R.id.monthText);
        mCapturedImageView = rootView.findViewById(R.id.capturedImageView);
        ImageButton cameraButton = rootView.findViewById(R.id.cameraButton);
        ImageButton saveButton = rootView.findViewById(R.id.saveButton);
        mStickerFrameLayout = rootView.findViewById(R.id.stickerFrameLayout);
        ImageButton rainbowButton = rootView.findViewById(R.id.buttonRainbow);
        ImageButton dancerButton = rootView.findViewById(R.id.buttonDancer);
        ImageButton glassesButton = rootView.findViewById(R.id.buttonGlasses);
        ImageButton heartButton = rootView.findViewById(R.id.buttonHeart);
        ImageButton crownButton = rootView.findViewById(R.id.buttonCrown);
        ImageButton jorisButton = rootView.findViewById(R.id.buttonJoris);

        mMonthText.setVisibility(View.INVISIBLE);
        createImageFolders();

        View.OnClickListener stickerListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSticker(v.getId());
            }
        };
        rainbowButton.setOnClickListener(stickerListener);
        dancerButton.setOnClickListener(stickerListener);
        glassesButton.setOnClickListener(stickerListener);
        heartButton.setOnClickListener(stickerListener);
        crownButton.setOnClickListener(stickerListener);
        jorisButton.setOnClickListener(stickerListener);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage();
            }
        });

        return rootView;
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {"Select photo from device", "Capture photo with camera"};
        pictureDialog.setItems(pictureDialogItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        pickImage();
                        break;
                    case 1:
                        takePhoto();
                        break;
                }
            }
        });
        pictureDialog.show();
    }

    private void pickImage() {
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK);
        pickImageIntent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        pickImageIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        pickImageIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(pickImageIntent, GALLERY_REQUEST_CODE);
        mMonthText.setVisibility(View.INVISIBLE);
    }

    private void takePhoto() {
        Intent callCameraApplicationIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (callCameraApplicationIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File file = null;
            try {
                file = createCacheFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (file != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(), "com.jorisvanlaar.employeeofthemonth.fileprovider", file);
                callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(callCameraApplicationIntent, CAMERA_REQUEST_CODE);
                mMonthText.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    Bitmap bitmap = null;
                    if (data.getData() != null) {
                        try {
                            bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(data.getData()));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        writeBitmapToFile(bitmap, createCacheFile());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    rotateImage(reduceImageSize());
                    break;

                case CAMERA_REQUEST_CODE:
                    rotateImage(reduceImageSize());
                    break;
            }
        }
    }

    private void createImageFolders() {
        File storageDirectory = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        mCacheFolder = new File(storageDirectory, "cached files");
        if (!mCacheFolder.exists()) {
            mCacheFolder.mkdirs();
        }
        mGalleryFolder = new File(storageDirectory, "Image Gallery");
        if (!mGalleryFolder.exists()) {
            mGalleryFolder.mkdirs();
        }
    }

    @SuppressLint("SimpleDateFormat")
    private File createCacheFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String cacheFileName = "IMG_" + timeStamp + "_";
        File image = File.createTempFile(cacheFileName, ".jpg", mCacheFolder);
        mCacheFileLocation = image.getAbsolutePath();
        return image;
    }

    @SuppressLint("SimpleDateFormat")
    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "EMPL_" + timeStamp + "_.jpg";
        File image = new File(mGalleryFolder, imageFileName);
        return image;
    }

    private void writeBitmapToFile(Bitmap bitmap, File destination) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(destination);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private Bitmap reduceImageSize() {
        int targetImageViewWidth = mCapturedImageView.getWidth();
        int targetImageViewHeight = mCapturedImageView.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCacheFileLocation, bmOptions);
        int cameraImageWidth = bmOptions.outWidth;
        int cameraImageHeight = bmOptions.outHeight;

        int scaleFactor = Math.min(cameraImageWidth / targetImageViewWidth, cameraImageHeight / targetImageViewHeight);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        Bitmap photoReducedSizeBitmap = BitmapFactory.decodeFile(mCacheFileLocation, bmOptions);
        return photoReducedSizeBitmap;
    }

    private void rotateImage(Bitmap bitmap) {
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(mCacheFileLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = 0;
        if (exifInterface != null) {
            orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        }
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(270);
                break;
            default:
        }
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        mCapturedImageView.setImageBitmap(rotatedBitmap);
    }

    @SuppressLint("SetTextI18n")
    private void saveImage() {
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);
        MonthCollection monthCollection = new MonthCollection();
        mMonthText.setText("Employee of " + monthCollection.getMonth(currentMonth));
        mMonthText.setVisibility(View.VISIBLE);

        Bitmap bitmap = Bitmap.createBitmap(mStickerFrameLayout.getWidth(), mStickerFrameLayout.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        mStickerFrameLayout.draw(canvas);

        writeBitmapToFile(bitmap, createImageFile());
        Toast.makeText(getActivity(), "Image saved!", Toast.LENGTH_SHORT).show();
    }

    private void addSticker(int id) {
        StickerImageView sticker = new StickerImageView(getActivity());
        switch (id) {
            case R.id.buttonRainbow:
                sticker.setImageDrawable(getResources().getDrawable(R.drawable.sticker_rainbow));
                break;
            case R.id.buttonDancer:
                sticker.setImageDrawable(getResources().getDrawable(R.drawable.sticker_dancer));
                break;
            case R.id.buttonGlasses:
                sticker.setImageDrawable(getResources().getDrawable(R.drawable.sticker_sunglasses));
                break;
            case R.id.buttonHeart:
                sticker.setImageDrawable(getResources().getDrawable(R.drawable.sticker_heart));
                break;
            case R.id.buttonCrown:
                sticker.setImageDrawable(getResources().getDrawable(R.drawable.sticker_crown));
                break;
            case R.id.buttonJoris:
                sticker.setImageDrawable(getResources().getDrawable(R.drawable.sticker_joris));
                break;
            default:
                throw new RuntimeException("Unknown button ID");
        }
        mStickerFrameLayout.addView(sticker);
    }
}

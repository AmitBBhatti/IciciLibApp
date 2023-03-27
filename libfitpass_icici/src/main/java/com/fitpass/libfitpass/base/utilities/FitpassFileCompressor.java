package com.fitpass.libfitpass.base.utilities;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.io.IOException;


public class FitpassFileCompressor {
    //max width and height values of the compressed image is taken as 612x816

    private int maxWidth = 412;
    private int maxHeight = 716;
    private Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
    private int quality = 60;
    private String destinationDirectoryPath;

    public FitpassFileCompressor(Context context) {
        destinationDirectoryPath = context.getCacheDir().getPath() + File.separator + "images";
    }

    public FitpassFileCompressor setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        return this;
    }

    public FitpassFileCompressor setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        return this;
    }

    public FitpassFileCompressor setCompressFormat(Bitmap.CompressFormat compressFormat) {
        this.compressFormat = compressFormat;
        return this;
    }

    public FitpassFileCompressor setQuality(int quality) {
        this.quality = quality;
        return this;
    }

    public FitpassFileCompressor setDestinationDirectoryPath(String destinationDirectoryPath) {
        this.destinationDirectoryPath = destinationDirectoryPath;
        return this;
    }

    public File compressToFile(File imageFile) throws IOException {
        return compressToFile(imageFile, imageFile.getName());
    }
    public File compressToFontFile2(File imageFile) throws IOException {
        return compressToFontFile2(imageFile, imageFile.getName());
    }
    public File compressToFontFile3(File imageFile) throws IOException {
        return compressToFontFile3(imageFile, imageFile.getName());
    }
    public File compressToFile(File imageFile, String compressedFileName) throws IOException {
        return FitpassImageUtil.compressImage(imageFile, maxWidth, maxHeight, compressFormat, quality,
                destinationDirectoryPath + File.separator + compressedFileName);
    }
    public File compressToFontFile2(File imageFile, String compressedFileName) throws IOException {
        return FitpassImageUtil.compressImage(imageFile, maxWidth, maxHeight, compressFormat, quality,
                destinationDirectoryPath + File.separator + compressedFileName);
    }
    public File compressToFontFile3(File imageFile, String compressedFileName) throws IOException {
        return FitpassImageUtil.compressImage3(imageFile, maxWidth, maxHeight, compressFormat, quality,
                destinationDirectoryPath + File.separator + compressedFileName);
    }

    public Bitmap compressToBitmap(File imageFile) throws IOException {
        return FitpassImageUtil.decodeSampledBitmapFromFile(imageFile, maxWidth, maxHeight);
    }
}
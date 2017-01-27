package rustam.evade.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

/**
 * Created by RUSTAM on 2017-01-16.
 */

public class TextureHelper {

    public static int loadTexture(Context context, int resourceID) {
        int[] textureID = new int[1];
        GLES20.glGenTextures(1, textureID, 0);

        if (textureID[0] != 0) {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;

            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceID, options);

            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureID[0]);
            // Filters
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            // Load texture to bound texture buffer
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

            GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);

            bitmap.recycle();
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        }
        if (textureID[0] == 0) {
            throw new RuntimeException("Error loading texture.");
        }
        return textureID[0];
    }
}

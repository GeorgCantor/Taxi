package taxi.kassa.view.registration.photo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.Image
import android.os.Bundle
import android.provider.MediaStore
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_photo.*
import org.koin.androidx.viewmodel.ext.android.getSharedViewModel
import taxi.kassa.R
import taxi.kassa.util.gone
import taxi.kassa.util.runDelayed
import taxi.kassa.util.shortToast
import taxi.kassa.util.visible
import taxi.kassa.view.registration.connection.ConnectionViewModel
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class PhotoFragment : Fragment(R.layout.fragment_photo) {

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    private val permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private val executor: Executor by lazy { Executors.newSingleThreadExecutor() }

    private var imageCapture: ImageCapture? = null
    private var lensFacing = CameraX.LensFacing.BACK
    private var loadFromGallery = false

    private lateinit var viewModel: ConnectionViewModel
    private lateinit var photoBitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getSharedViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissions()

        reverse_camera.setOnClickListener { toggleFrontBackCamera() }
        take_photo_btn.setOnClickListener { takePicture() }
        reshoot_title.setOnClickListener { setVisibilityWhileShooting() }
        folder.setOnClickListener { openGallery() }

        done.setOnClickListener { activity?.onBackPressed() }
        back_arrow.setOnClickListener { activity?.onBackPressed() }
    }

    override fun onDetach() {
        super.onDetach()
        if (::photoBitmap.isInitialized && !loadFromGallery) viewModel.sendPhoto(photoBitmap)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                preview_view.post { startCamera() }
            } else {
                activity?.onBackPressed()
            }
        }
    }

    private fun requestPermissions() {
        if (allPermissionsGranted()) {
            preview_view.post { startCamera() }
        } else {
            requestPermissions(permissions, REQUEST_CODE_PERMISSIONS)
        }
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)

        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    private fun imageToBitmap(image: Image): Bitmap {
        val buffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.capacity())
        buffer.get(bytes)

        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size, null)
    }

    private fun setVisibilityWhileShooting() {
        preview_view.visible()
        taken_image.gone()
        done.gone()
        reshoot_title.gone()
        reverse_camera.visible()
        take_photo_btn.isClickable = true
    }

    private fun setVisibilityAfterShooting() {
        preview_view.gone()
        taken_image.visible()
        done.visible()
        reshoot_title.visible()
        reverse_camera.gone()
        take_photo_btn.isClickable = false
    }

    private fun allPermissionsGranted() = permissions.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PERMISSION_GRANTED
    }

    private fun startCamera() {
        CameraX.unbindAll()

        val preview = createPreviewUseCase()
        preview.setOnPreviewOutputUpdateListener {
            val parent = preview_view.parent as ViewGroup
            parent.removeView(preview_view)
            parent.addView(preview_view, 0)

            preview_view.setSurfaceTexture(it.surfaceTexture)
            updateTransform()
        }
        imageCapture = createCaptureUseCase()

        CameraX.bindToLifecycle(this, preview, imageCapture)
    }

    private fun openGallery() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val inputStream = data.data?.let { requireActivity().contentResolver.openInputStream(it) }
                val bitmap = BitmapFactory.decodeStream(inputStream)
                viewModel.sendPhoto(bitmap)
                loadFromGallery = true

                200L.runDelayed { activity?.onBackPressed() }
            }
        }
    }

    private fun createPreviewUseCase(): Preview {
        val previewConfig = PreviewConfig.Builder().apply {
            setLensFacing(lensFacing)
            setTargetRotation(preview_view.display.rotation)
        }.build()

        return Preview(previewConfig)
    }

    private fun updateTransform() {
        val matrix = Matrix()
        val centerX = preview_view.width / 2f
        val centerY = preview_view.height / 2f

        val rotationDegrees = when (preview_view.display.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> return
        }
        matrix.postRotate(-rotationDegrees.toFloat(), centerX, centerY)

        preview_view.setTransform(matrix)
    }

    private fun createCaptureUseCase(): ImageCapture {
        val imageCaptureConfig = ImageCaptureConfig.Builder()
            .apply {
                setLensFacing(lensFacing)
                setTargetRotation(preview_view.display.rotation)
                setCaptureMode(ImageCapture.CaptureMode.MAX_QUALITY)
            }

        return ImageCapture(imageCaptureConfig.build())
    }

    private fun toggleFrontBackCamera() {
        lensFacing = if (lensFacing == CameraX.LensFacing.BACK) {
            CameraX.LensFacing.FRONT
        } else {
            CameraX.LensFacing.BACK
        }
        preview_view.post { startCamera() }
    }

    private fun takePicture() {
        setVisibilityWhileShooting()
        savePictureToMemory()
    }

    private fun savePictureToMemory() {
        imageCapture?.takePicture(executor, object : ImageCapture.OnImageCapturedListener() {
                override fun onError(error: ImageCapture.ImageCaptureError, message: String, exc: Throwable?) {
                    context?.shortToast(message)
                }

                override fun onCaptureSuccess(imageProxy: ImageProxy?, rotationDegrees: Int) {
                    imageProxy?.image?.let {
                        val bitmap = rotateImage(imageToBitmap(it), rotationDegrees.toFloat())
                        photoBitmap = bitmap

                        requireActivity().runOnUiThread {
                            taken_image.setImageBitmap(bitmap)
                            setVisibilityAfterShooting()
                        }
                    }
                    super.onCaptureSuccess(imageProxy, rotationDegrees)
                }
            })
    }
}
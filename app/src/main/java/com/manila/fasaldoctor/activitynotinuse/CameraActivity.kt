package com.manila.fasaldoctor.activitynotinuse

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.media.ImageReader
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface
import android.view.TextureView
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.manila.fasaldoctor.R
import java.io.File
import java.io.FileOutputStream


class CameraActivity : AppCompatActivity() {

    lateinit var imageUri : Uri
    lateinit var btnRotate : ImageButton

    lateinit var btnCapture:ImageButton
//    private lateinit var binding: ActivityMainBinding
    lateinit var cameraManager: CameraManager
    lateinit var capReq: CaptureRequest
    lateinit var textureView: TextureView
    lateinit var cameraCaptureSession: CameraCaptureSession
    lateinit var cameraDevice: CameraDevice
    lateinit var cameraCaptureRequest: CaptureRequest
    lateinit var handler: Handler
    lateinit var handlerThread: HandlerThread
    lateinit var imageReader: ImageReader

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_camera)

        btnCapture = findViewById(R.id.btnCapture)
        btnRotate = findViewById(R.id.btnRotate)
        var btnRotateRear : Button = findViewById(R.id.btnRotateRear)

        textureView = findViewById(R.id.view_texture)
        cameraManager= getSystemService(Context.CAMERA_SERVICE) as CameraManager
        handlerThread = HandlerThread("image")
        handlerThread.start()
        handler = Handler((handlerThread).looper)
        textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener{
            override fun onSurfaceTextureAvailable(
                surface: SurfaceTexture,
                width: Int,
                height: Int
            ) {

                    btnRotateRear.setOnClickListener {
                        open_camera_Rear()

                    }

                btnRotate.setOnClickListener {

                    Toast.makeText(this@CameraActivity,"Rotate",Toast.LENGTH_SHORT).show()

                    open_camera_Front()
                }


            }

            override fun onSurfaceTextureSizeChanged(
                surface: SurfaceTexture,
                width: Int,
                height: Int
            ) {

            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                return false
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {

            }

        }

// code for save images in local
        imageReader = ImageReader.newInstance(1080,1920,ImageFormat.JPEG,1)
        imageReader.setOnImageAvailableListener(object : ImageReader.OnImageAvailableListener {
            override fun onImageAvailable(reader: ImageReader?) {
                var image = reader?.acquireLatestImage()
                var buffer = image?.planes?.get(0)?.buffer
                var bytes = ByteArray(buffer!!.remaining())
                buffer.get(bytes)

                var file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),"manila")
                var opStream = FileOutputStream(file)
                opStream.write(bytes)
                opStream.close()

                image?.close()

                Toast.makeText(this@CameraActivity, "Capturred", Toast.LENGTH_SHORT).show()


            }
        },handler)

        findViewById<ImageButton>(R.id.btnCapture).apply {
            setOnClickListener {
                var capReq = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
                capReq.addTarget(imageReader.surface)
                cameraCaptureSession.capture(capReq.build(),null,null)


            }

        }

//        btnCapture.setOnClickListener {
//            capReq = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
//
//        }
//        btnRotate.setOnClickListener {
//
//        }



    }

    override fun onDestroy() {
        super.onDestroy()
        cameraDevice.close()
        handler.removeCallbacksAndMessages(null)
        handlerThread.quitSafely()
    }

    // code for open camera

    @SuppressLint("MissingPermission")
    fun open_camera_Rear(){
        cameraManager.openCamera(cameraManager.cameraIdList[0],object : CameraDevice.StateCallback(){
            override fun onOpened(camera: CameraDevice) {
                cameraDevice = camera
                var surface = Surface(textureView.surfaceTexture)
                var capReq = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                capReq.addTarget(surface)



                cameraDevice.createCaptureSession(listOf(surface,imageReader.surface),object : CameraCaptureSession.StateCallback(){
                    override fun onConfigured(session: CameraCaptureSession) {
                        cameraCaptureSession = session
                        cameraCaptureSession.setRepeatingRequest(capReq.build(),null,null)

                    }

                    override fun onConfigureFailed(session: CameraCaptureSession) {
                        TODO("Not yet implemented")
                    }
                },handler)


            }

            override fun onDisconnected(camera: CameraDevice) {

            }

            override fun onError(camera: CameraDevice, error: Int) {

            }


        },handler
        )
    }


    @SuppressLint("MissingPermission")
    fun open_camera_Front(){
        cameraManager.openCamera(cameraManager.cameraIdList[1],object : CameraDevice.StateCallback(){
            override fun onOpened(camera: CameraDevice) {
                cameraDevice = camera
                var surface = Surface(textureView.surfaceTexture)
                var capReq = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                capReq.addTarget(surface)



                cameraDevice.createCaptureSession(listOf(surface,imageReader.surface),object : CameraCaptureSession.StateCallback(){
                    override fun onConfigured(session: CameraCaptureSession) {
                        cameraCaptureSession = session
                        cameraCaptureSession.setRepeatingRequest(capReq.build(),null,null)

                    }

                    override fun onConfigureFailed(session: CameraCaptureSession) {
                        TODO("Not yet implemented")
                    }
                },handler)


            }

            override fun onDisconnected(camera: CameraDevice) {

            }

            override fun onError(camera: CameraDevice, error: Int) {

            }


        },handler
        )
    }

//    private fun createImageUri(): Uri? {
//        val image = File(applicationContext.filesDir,"camera_photo.png")
//        return FileProvider.getUriForFile(applicationContext,"com.manila.fasaldoctor.fileProviderImage",
//            image)
//    }

}
























































//        if (allPermissionGranted()) {
//            Toast.makeText(this, "PERMISSION GRANTED", Toast.LENGTH_SHORT).show()
//        } else {
//            ActivityCompat.requestPermissions(
//                this, Constants.REQUIRED_PERMISSIONS,
//                Constants.REQUEST_CODE_PERMISSION
//            )
//
//        }




//    private fun allPermissionGranted() =
//        Constants.REQUIRED_PERMISSIONS.all {
//            ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
//
//
//
//    }


package com.example.arproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arproject.adapter.ActionAdapter;
import com.example.arproject.model.ARContent;
import com.example.arproject.model.Action;
import com.example.arproject.model.CustomArFragment;
import com.example.arproject.model.Marker;
import com.example.arproject.model.TextARContent;
import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.AugmentedImageDatabase;
import com.google.ar.core.Config;
import com.google.ar.core.Frame;
import com.google.ar.core.Pose;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ExternalTexture;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LearnActivity extends AppCompatActivity implements Scene.OnUpdateListener {
    private CustomArFragment arFragment;
    private TextView textView;
    private List <Marker> markerList;
    private boolean scanned = false;
    private ArrayList<AnchorNode> anchorNodeArrayList = new ArrayList<>();
    private int currentMarkerID = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        textView = findViewById(R.id.textView);
        arFragment = (CustomArFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        arFragment.getArSceneView().getScene().addOnUpdateListener(this);
    }

    public void setupDatabase(Config config, Session session){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LearnActivity.this);
        String markerListString = preferences.getString("markerlist",null);
        Type type = new TypeToken<List<Marker>>(){}.getType();
        markerList = new Gson().fromJson(markerListString,type);
        AugmentedImageDatabase aid = new AugmentedImageDatabase(session);
        for (Marker marker: markerList){
            List<Action> actionList = marker.getActionList();
            Bitmap bitmap = getBitmapFromURL(marker.getURL());
            aid.addImage(""+marker.getMarkerID(),bitmap);
        }
        config.setAugmentedImageDatabase(aid);
    }
    @Override
    public void onUpdate(FrameTime frameTime) {
        Frame frame = arFragment.getArSceneView().getArFrame();
        Collection<AugmentedImage> images = frame.getUpdatedTrackables(AugmentedImage.class);
        for (AugmentedImage image: images){
            if(image.getTrackingMethod()== AugmentedImage.TrackingMethod.FULL_TRACKING){
                for (Marker marker: markerList){
                    if (marker.getMarkerID() == Integer.parseInt(image.getName()) && scanned == false){
                        Log.i("vc",""+marker.getMarkerID());
                        Anchor anchor = image.createAnchor(image.getCenterPose());
                        List<Action> actionList = marker.getActionList();
                        for (Action action: actionList){
                            if (action.getName().equals("Khởi tạo")){
                                // get noi dung
                                List<ARContent> arContentList = action.getArContentList();
                                for (ARContent arContent : arContentList){
                                    String URL = arContent.getURL();
                                    char lastExtension = URL.charAt(URL.length()-1);
                                    if (lastExtension=='b'){
                                        create3DModel(arContent,anchor,image);
                                    }
                                    else if(URL.equals("text")){
                                        create2DText(arContent,anchor,image);
                                    }
                                    else if(lastExtension=='g'){
                                        createImage(arContent,anchor,image);
                                    } else if(lastExtension=='4'){
                                        createVideo(arContent,anchor,image);
                                    }
                                }
                            }
                        }
                        ActionAdapter adapter = new ActionAdapter(LearnActivity.this,R.layout.line_button_action,actionList);
                        ListView listViewAction = findViewById(R.id.listView_button);
                        listViewAction.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        Button btnNewContent = findViewById(R.id.btn_newContent);
                        btnNewContent.setVisibility(View.VISIBLE);
                        Button btn_content = findViewById(R.id.btn_content);
                        btnNewContent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                for (AnchorNode anchorNode: anchorNodeArrayList){
                                    arFragment.getArSceneView().getScene().removeChild(anchorNode);
                                    anchorNode.getAnchor().detach();
                                }
                                anchorNodeArrayList.clear();
                                listViewAction.setVisibility(View.INVISIBLE);
                                btnNewContent.setVisibility(View.INVISIBLE);
                                btn_content.setVisibility(View.INVISIBLE);
                                scanned = false;
                            }
                        });
                        btn_content.setVisibility(View.VISIBLE);
                        btn_content.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (listViewAction.getVisibility()==View.VISIBLE){
                                    listViewAction.setVisibility(View.INVISIBLE);
                                }else{
                                    listViewAction.setVisibility(View.VISIBLE);
                                }
                                listViewAction.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        for (AnchorNode anchorNode: anchorNodeArrayList){
                                            arFragment.getArSceneView().getScene().removeChild(anchorNode);
                                            anchorNode.getAnchor().detach();
                                        }
                                        anchorNodeArrayList.clear();
                                        // Render lai
                                        Action thisAction = actionList.get(position);
                                        List<ARContent> thisARContentList = thisAction.getArContentList();
                                        for (ARContent arContent : thisARContentList){
                                            String URL = arContent.getURL();
                                            char lastExtension = URL.charAt(URL.length()-1);
                                            if (lastExtension=='b'){
                                                create3DModel(arContent,anchor,image);
                                            }
                                            else if(URL.equals("text")){
                                                create2DText(arContent,anchor,image);
                                            }
                                            else if(lastExtension=='g'){
                                                createImage(arContent,anchor,image);
                                            } else if(lastExtension == '4'){
                                                createVideo(arContent,anchor,image);
                                            }
                                        }
                                    }
                                });
                            }
                        });
                        Log.i("he", marker.getURL());
                        scanned = true;
                    }
                }
                Log.i("pose",image.getCenterPose().toString());

            }
        }
    }
    // render 3D model
    private void create3DModel(ARContent arContent, Anchor anchor, AugmentedImage image) {
        AnchorNode anchorNode = setPropertyAnchorNode(arContent,anchor,image);
        ModelRenderable.builder()
                .setSource(this,
                        RenderableSource.builder().setSource(
                                this,
                                Uri.parse(arContent.getURL()),
                                RenderableSource.SourceType.GLB)
                                .setScale(1)
                                .setRecenterMode(RenderableSource.RecenterMode.CENTER)
                                .build())
                .setRegistryId(arContent.getURL())
                .build()
                .thenAccept(renderable -> place3DModel(renderable,anchorNode))
                .exceptionally(throwable -> {
                    Toast.makeText(LearnActivity.this,"Can't load the model",Toast.LENGTH_SHORT);
                    return null;
                });
    }

    private AnchorNode setPropertyAnchorNode(ARContent arContent, Anchor anchor, AugmentedImage image) {
        AnchorNode anchorNode = new AnchorNode();
//        // get world postition
        float[] translation = anchor.getPose().getTranslation();
        float[] rotation = anchor.getPose().getRotationQuaternion();
        Vector3 scale = new Vector3(arContent.getxScale(),arContent.getyScale(),arContent.getzScale());
        // cal rotation
        Quaternion worldQuaternion = new Quaternion(rotation[0],rotation[1],rotation[2],rotation[3]);
        Quaternion rotationX = Quaternion.axisAngle(new Vector3(1.0f, 0.0f, 0.0f), (arContent.getxRotation()*180)/(float)Math.PI);
        Quaternion rotationY = Quaternion.axisAngle(new Vector3(0.0f, 1.0f, 0.0f), (arContent.getyRotation()*180)/(float)Math.PI);
        Quaternion rotationZ = Quaternion.axisAngle(new Vector3(0.0f, 0.0f, 1.0f), (arContent.getzRotation()*180)/(float)Math.PI);
        Quaternion combineRotationX = Quaternion.multiply(worldQuaternion,rotationX);
        Quaternion combineRotationY = Quaternion.multiply(combineRotationX,rotationY);
        Quaternion combineRotationZ = Quaternion.multiply(combineRotationY,rotationZ);
        float[] worldPosition = new float[]{translation[0]+arContent.getxPosition(),translation[1]+arContent.getyPosition(),translation[2]+arContent.getzPosition()};
        float[] worldRotation = new float[]{combineRotationZ.x,combineRotationZ.y,combineRotationZ.z,combineRotationZ.w};
        Pose pose1 = new Pose(worldPosition,worldRotation);
        Anchor anchor1 = image.createAnchor(pose1);
        anchorNode.setLocalScale(scale);
        anchorNode.setAnchor(anchor1);
        return anchorNode;
    }

    private AnchorNode setPropertyPixelAnchorNode(ARContent arContent, Anchor anchor, AugmentedImage image) {
        AnchorNode anchorNode = new AnchorNode();
//        // get world postition
        float[] translation = anchor.getPose().getTranslation();
        float[] rotation = anchor.getPose().getRotationQuaternion();
        Vector3 scale = new Vector3(arContent.getxScale(),arContent.getyScale(),arContent.getzScale());
        Vector3 scaleView = new Vector3(0.263f*arContent.getxScale(),0.263f*arContent.getyScale(),0.263f*arContent.getzScale());
        // cal rotation
        Quaternion worldQuaternion = new Quaternion(rotation[0],rotation[1],rotation[2],rotation[3]);
        Quaternion rotationX = Quaternion.axisAngle(new Vector3(1.0f, 0.0f, 0.0f), (arContent.getxRotation()*180)/(float)Math.PI);
        Quaternion rotationY = Quaternion.axisAngle(new Vector3(0.0f, 1.0f, 0.0f), (arContent.getyRotation()*180)/(float)Math.PI);
        Quaternion rotationZ = Quaternion.axisAngle(new Vector3(0.0f, 0.0f, 1.0f), (arContent.getzRotation()*180)/(float)Math.PI);
        Quaternion combineRotationX = Quaternion.multiply(worldQuaternion,rotationX);
        Quaternion combineRotationY = Quaternion.multiply(combineRotationX,rotationY);
        Quaternion combineRotationZ = Quaternion.multiply(combineRotationY,rotationZ);
        float[] worldPosition = new float[]{translation[0]+arContent.getxPosition(),translation[1]+arContent.getyPosition(),translation[2]+arContent.getzPosition()};
        float[] worldRotation = new float[]{combineRotationZ.x,combineRotationZ.y,combineRotationZ.z,combineRotationZ.w};
        Pose pose1 = new Pose(worldPosition,worldRotation);
        Anchor anchor1 = image.createAnchor(pose1);
        anchorNode.setLocalScale(scaleView);
        anchorNode.setAnchor(anchor1);
        return anchorNode;
    }

    private void place3DModel(ModelRenderable modelRenderable, AnchorNode anchorNode){
        anchorNode.setRenderable(modelRenderable);
        Log.i("hoho",anchorNode.getWorldPosition().toString());
        arFragment.getArSceneView().getScene().addChild(anchorNode);
        anchorNodeArrayList.add(anchorNode);
    }
    private void create2DText(ARContent arContent, Anchor anchor, AugmentedImage image) {
        AnchorNode anchorNode = setPropertyAnchorNode(arContent,anchor,image);
        ViewRenderable
                .builder()
                .setView(LearnActivity.this,R.layout.custom_text)
                .setVerticalAlignment(ViewRenderable.VerticalAlignment.CENTER)
                .setHorizontalAlignment(ViewRenderable.HorizontalAlignment.CENTER)
                .build()
                .thenAccept(viewRenderable -> placeTextView(viewRenderable,anchorNode,arContent));
    }
    private void placeTextView(ViewRenderable viewRenderable, AnchorNode anchorNode,ARContent arContent) {
        View viewCustomText = viewRenderable.getView();
        TextView txvCustom = viewCustomText.findViewById(R.id.txv_customText);
        // Set custom text
        TextARContent textARContent = arContent.getTextARContent();
        txvCustom.setText(textARContent.getText());
        txvCustom.setTextColor(Color.parseColor(textARContent.getColor()));
        txvCustom.setBackgroundColor(Color.parseColor(textARContent.getBackgroundColor()));
        float textSize = textARContent.getSize();
        txvCustom.setTextSize(textSize);
        int paddingTop = (int)textSize;
        int paddingLeft = (int) textSize*5/4;
        txvCustom.setPadding(paddingLeft,paddingTop,paddingLeft,paddingTop);
        anchorNode.setRenderable(viewRenderable);
        Vector3 scaleView = new Vector3(0.065f,0.065f,0.065f);
        anchorNode.setLocalScale(scaleView);
        anchorNodeArrayList.add(anchorNode);
        arFragment.getArSceneView().getScene().addChild(anchorNode);
    }
    private void createImage(ARContent arContent, Anchor anchor, AugmentedImage image) {
        AnchorNode anchorNode = setPropertyPixelAnchorNode(arContent,anchor,image);
        ViewRenderable
                .builder()
                .setView(LearnActivity.this,R.layout.custom_image)
                .setVerticalAlignment(ViewRenderable.VerticalAlignment.CENTER)
                .setHorizontalAlignment(ViewRenderable.HorizontalAlignment.CENTER)
                .build()
                .thenAccept(viewRenderable -> placeImage(viewRenderable,anchorNode,arContent));
    }
    private void placeImage(ViewRenderable viewRenderable, AnchorNode anchorNode, ARContent arContent) {
        View viewCustomImage = viewRenderable.getView();
        ImageView imvCustom = viewCustomImage.findViewById(R.id.imv_customeImage);
        Bitmap bitmap = getBitmapFromURL(arContent.getURL());
        imvCustom.setImageBitmap(bitmap);
        anchorNode.setRenderable(viewRenderable);
        anchorNodeArrayList.add(anchorNode);
        arFragment.getArSceneView().getScene().addChild(anchorNode);
    }
    private void createVideo(ARContent arContent, Anchor anchor,AugmentedImage image){
        AnchorNode anchorNode = setPropertyPixelAnchorNode(arContent,anchor,image);
        textView.setText("haha");
        ModelRenderable.builder()
                .setSource(this,R.raw.chroma_key_video1)
                .build()
                .thenAccept(modelRenderable -> placeVideo(modelRenderable,anchorNode,arContent));
    }

    private void placeVideo(ModelRenderable modelRenderable, AnchorNode anchorNode, ARContent arContent) {
        ExternalTexture texture = new ExternalTexture();
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.videotest);
        mediaPlayer.setSurface(texture.getSurface());
        mediaPlayer.setLooping(true);
        modelRenderable.getMaterial().setExternalTexture("videoTexture", texture);
        modelRenderable.getMaterial().setFloat4("keyColor", new com.google.ar.sceneform.rendering.Color(0.01843f, 1.0f,0.098f));
        anchorNode.setRenderable(modelRenderable);
        float HEIGHT = 0.15f;
        float width = mediaPlayer.getVideoWidth();
        float height = mediaPlayer.getVideoHeight();

        anchorNode.setLocalScale(new Vector3(HEIGHT * (width / height), HEIGHT, 0.95f));
        anchorNodeArrayList.add(anchorNode);
        arFragment.getArSceneView().getScene().addChild(anchorNode);
    }

    public static Bitmap getBitmapFromURL(String src){
        try {
            URL url = new URL(src);

            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            Log.d("vk21", e.toString());
            return null;
        }
    }

}
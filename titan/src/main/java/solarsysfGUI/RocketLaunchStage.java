package solarsysfGUI;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import com.interactivemesh.jfx.importer.tds.TdsModelImporter;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import physics.ODEsolver;
import physics.VerletVelocity;
import solarsystem.*;
import solarsystem.SolarSystem;
import solarsystem.rocket.Projectile;
import solarsystem.rocket.mainRocket.InterPlanetaryRocketToEarth;
import solarsystem.rocket.mainRocket.InterPlanetaryRocketToTitanFlyByJupiter;
import utils.Date;
import utils.MathUtil;
import utils.vector.Vector3D;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * This is a clean InterPlanetaryRocketToTitan launching stage, to be introduced functionalities
 */
public class RocketLaunchStage extends Application {
    // running variables
    private boolean pauseStatus = false;

    // Timing variables
    private Date date;
    private Date pauseDate;
    private boolean pausedForDate = false;
    private final long dt = 30;
    private final TimeUnit timeUnit = TimeUnit.SECONDS;
    private double UPDATE_FREQUENCY_IN_MS = 0.1;
    // GUI variables
    private Scene mainScene;
    private Group root;
    private boolean goNorth, goSouth, goEast, goWest;
    private final boolean USELIGHTS = true;
    private Label identifierLabel;
    private static final String FILE_LOC = "src/main/resources/rocketObj/scifi_cartoon_rocket.obj";

    private HashMap<Group, Projectile> projectileList = new HashMap<>();
    private SolarSystem solarSystem;
    private CelestialObject followObject;

    private int DistanceMultiplier = 40;
    private double plntRadFact = (2.0/6371.0);

    private ODEsolver odEsolver = new VerletVelocity();

    @Override
    public void start(Stage primaryStage) {
        try {
            solarSystem = toTitanTrajectory();
            date = solarSystem.getCurrentDate();
        } catch (IOException e) {
            e.printStackTrace();
        }

        CelestialObject earthObj = solarSystem.getPlanets().getEarth();
        CelestialObject sunObj = solarSystem.getPlanets().getSun();
        CelestialObject mercuryObj = solarSystem.getPlanets().getMercury();
        CelestialObject marsObj = solarSystem.getPlanets().getMars();
        CelestialObject jupiterObj = solarSystem.getPlanets().getJupiter();
        CelestialObject saturnObj = solarSystem.getPlanets().getSaturn();
        CelestialObject titanObj = solarSystem.getPlanets().getTitan();
        CelestialObject uranusObj = solarSystem.getPlanets().getUranus();
        CelestialObject neptuneObj = solarSystem.getPlanets().getNeptune();
        CelestialObject venusObj = solarSystem.getPlanets().getVenus();
        for (Projectile projObj: solarSystem.getProjectiles()) {

            double rocketScale = 0.2;
            Group rocket = loadRocket();
            rocket.setScaleX(rocketScale);
            rocket.setScaleY(rocketScale);
            rocket.setScaleZ(rocketScale);
            rocket.setTranslateX(20);
            rocket.setTranslateY(-40);


            followObject = projObj;
            projectileList.put(rocket, projObj);
        }

//        followObject = sunObj; //set followup object as sun (the middle planet)

        //Create the planet spheres
        Sphere sun = createGUIobject(sunObj, new Image("textures/sunmap.jpg"), null);
        Sphere earth = createGUIobject(earthObj, new Image("textures/earthmap.jpg"), new Image("textures/earthbump.jpg"));
        Sphere mercury = createGUIobject(mercuryObj, new Image("textures/mercurymap.jpg"), new Image("textures/mercurybump.jpg"));
        Sphere mars = createGUIobject(marsObj, new Image("textures/marsmap.jpg"), new Image("textures/marsbump.jpg"));
        Sphere jupiter = createGUIobject(jupiterObj, new Image("textures/jupitermap.jpg"), null);
        Sphere saturn = createGUIobject(saturnObj, new Image("textures/saturnmap.jpg"), null);
        Sphere titan = createGUIobject(titanObj, new Image("textures/moonmap.jpg"), new Image("textures/moonbump.jpg"));
        Sphere uranus = createGUIobject(uranusObj, new Image("textures/uranusmap.jpg"), null);
        Sphere neptune = createGUIobject(neptuneObj, new Image("textures/neptunemap.jpg"), null);
        Sphere venus = createGUIobject(venusObj, new Image("textures/venusmap.jpg"), new Image("textures/venusbump.jpg"));
        ((PhongMaterial) titan.getMaterial()).setDiffuseColor(Color.LIGHTGOLDENRODYELLOW); //add titan a yellow tint

        //Planet Name Label
        identifierLabel = new Label();
        identifierLabel.setTextFill(Color.WHITE);

        //Date Label
        Label dateLabel = new Label(date.toDateString());
        dateLabel.setTextFill(Color.WHITE);

        //Pause/Play button
        Hyperlink pausePlayButton = new Hyperlink("❚❚");
        pausePlayButton.setTextFill(Color.WHITE);
        pausePlayButton.setBorder(null);
        pausePlayButton.setOnAction(e -> {
            pauseStatus = !pauseStatus;
            if (pauseStatus) pausePlayButton.setText("▶");
            else pausePlayButton.setText("❚❚");
        });

        //Light fx
        PointLight pointLight = null;
        AmbientLight ambientLight = null;
        if (USELIGHTS) {
            pointLight = new PointLight(Color.WHITE);
            pointLight.setTranslateX(0);
            pointLight.setTranslateY(0);
            pointLight.setTranslateZ(0);
            ambientLight = new AmbientLight();
        }

        //Camera
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-100);
        camera.setNearClip(0.0001);
        camera.setFarClip(2000);
        camera.setFieldOfView(35);

        //root has the celestialobjects and the lights.
        root = new Group();
        if (USELIGHTS) root.getChildren().addAll(pointLight, ambientLight);
        root.getChildren().addAll(sun, earth, mercury, mars, jupiter, saturn, titan, uranus, neptune, venus);
        root.getChildren().addAll(projectileList.keySet());


        AnchorPane globalRoot = new AnchorPane();
        globalRoot.getChildren().addAll(dateLabel, identifierLabel, pausePlayButton);
        //Anchor the dateLabel
        globalRoot.setBottomAnchor(dateLabel, 10D);
        globalRoot.setRightAnchor(dateLabel, 10D);
        globalRoot.setLeftAnchor(identifierLabel, 10D);
        globalRoot.setTopAnchor(identifierLabel, 10D);

        SubScene subScene = new SubScene(root,800,600,false, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);
        globalRoot.getChildren().add(subScene);


        mainScene = new Scene(globalRoot, 800, 600, true);
        globalRoot.setStyle("-fx-background-image: url('textures/galaxy_starfield.png');");
        primaryStage.setTitle("SolarSysF - Solar Visualization From Scratch");
        primaryStage.setScene(mainScene);

        mainScene.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override public void handle(ScrollEvent event) {
                event.consume();
                if (event.getDeltaY() == 0) return;
                else if (event.getDeltaY() > 0) camera.setTranslateZ((camera.getTranslateZ() * 0.9));
                else camera.setTranslateZ((camera.getTranslateZ() *1.1));
            }
        });

        //KeyUp&KeyDown https://stackoverflow.com/questions/29962395/how-to-write-a-keylistener-for-javafx
        mainScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:    goNorth = true; break;
                    case DOWN:  goSouth = true; break;
                    case LEFT:  goWest  = true; break;
                    case RIGHT: goEast  = true; break;
                }
            }
        });

        mainScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:    goNorth = false; break;
                    case DOWN:  goSouth = false; break;
                    case LEFT:  goWest  = false; break;
                    case RIGHT: goEast  = false; break;

                    // change planet radius
                    case EQUALS: plntRadFact = plntRadFact * 1.1; break;
                    case MINUS: plntRadFact = plntRadFact * 0.9; break;
                    case DIGIT0: camera.setTranslateX(0); camera.setTranslateY(0); break;

                    case S: followObject = sunObj; camera.setTranslateX(0); camera.setTranslateY(0); break;
                    case E: followObject = earthObj; camera.setTranslateX(0); camera.setTranslateY(0);break;
                    case R: followObject = saturnObj; camera.setTranslateX(0); camera.setTranslateY(0);break;
                    case T: followObject = titanObj; camera.setTranslateX(0); camera.setTranslateY(0);break;

                    case DIGIT1: DistanceMultiplier = 40; plntRadFact = (1.0/6371.0); camera.setTranslateZ(-100); break;
                    case DIGIT2: DistanceMultiplier = 1; plntRadFact = (1000.0/MathUtil.AU); camera.setTranslateZ(-0.02); break;

                    case F7: UPDATE_FREQUENCY_IN_MS = UPDATE_FREQUENCY_IN_MS*2D;
                    case F9: UPDATE_FREQUENCY_IN_MS = UPDATE_FREQUENCY_IN_MS/2D;


                }
            }
        });

//        ArrayList<CelestialObject> allObj =
//                new ArrayList<>(projectileList.values());
//        allObj.addAll(solarSystem.getPlanets().getAll());
//        solarSystem.setAllAnimatedObjects(allObj);
//        solarSystem.setODEsolver(odEsolver);
//
//        ArrayList<Projectile> projectileInstances = new ArrayList<>(projectileList.values());

//        solarSystem.initializeAnimationWithPlanets(date, projectileInstances);
        class ODEupdater implements Runnable {
            private long lastUpdate = System.nanoTime();

            public void run() {
                while(!pauseStatus) {
                    if (System.nanoTime() - lastUpdate >= UPDATE_FREQUENCY_IN_MS * 1000000) {
                        solarSystem.updateAnimation(dt, timeUnit);
                        lastUpdate = System.nanoTime();
                    }
                }
            }
        }
        Thread ODEu = new Thread(new ODEupdater());
        ODEu.start();

        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                dateLabel.setText(date.toDateString());
                Vector3D coordinate;

                //have to update the sun manually because it's enormous
                coordinate = sunObj.getCentralPos().substract(followObject.getCentralPos());
                sun.setRadius(sunObj.getRadius() / 1000 * plntRadFact/50.0);
                sun.setTranslateX(coordinate.getX() * DistanceMultiplier / MathUtil.AU);
                sun.setTranslateY(coordinate.getY() * DistanceMultiplier * -1 / MathUtil.AU);
                sun.setTranslateZ(coordinate.getZ() * DistanceMultiplier / MathUtil.AU);
                sun.setRotate(sun.getRotate() + 2);

                //updateGUIobject(sun, sunObj);
                updateGUIobject(earth, earthObj);
                updateGUIobject(mercury, mercuryObj);
                updateGUIobject(mars, marsObj);
                updateGUIobject(jupiter, jupiterObj);
                updateGUIobject(saturn, saturnObj);
                updateGUIobject(titan, titanObj);
                updateGUIobject(uranus, uranusObj);
                updateGUIobject(neptune, neptuneObj);
                updateGUIobject(venus, venusObj);

                //Update projectile positions & check whether it's close enough to titan to activate landing phase
                for(Group guiObject: projectileList.keySet()){
                    updateRocket(guiObject, projectileList.get(guiObject));

                    if (projectileList.get(guiObject).phaseFinished()) { //i love to play with this variable, and you will too.
                        pauseStatus = true;
                        System.out.println("Initiate the landing phase capt'!");
                        this.stop();
                        try {
                            //Application landingPhaseApp =
                                    //new LandingPhase(projectileList.get(guiObject), date);
                            //landingPhaseApp.start(new Stage());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                if(!pausedForDate && date.getTimeInMillis()>pauseDate.getTimeInMillis()){
                    pauseStatus = true;
                    pausedForDate = true;
                }

                //Move the camera
                if (goNorth) camera.setTranslateY(camera.getTranslateY() - 1);
                if (goSouth) camera.setTranslateY(camera.getTranslateY() + 1);
                if (goEast)  camera.setTranslateX(camera.getTranslateX() + 1);
                if (goWest)  camera.setTranslateX(camera.getTranslateX() - 1);
            }
        }.start();


        primaryStage.show();
    }
    public static void main(String args[]){
        launch(args);
    }


    private Sphere createGUIobject(CelestialObject object, Image diffuseMap, Image bumpMap){
        Rotate rotate = new Rotate(90, 0, 0, 0, Rotate.X_AXIS);
        Sphere guiOb = new Sphere(object.getRadius() /1000 * plntRadFact);
        guiOb.getTransforms().add(rotate);
        PhongMaterial guiMat = new PhongMaterial();
        //if no texture, apply purple as diffmap
        if (diffuseMap == null && bumpMap == null) guiMat.setDiffuseColor(Color.PURPLE);
        guiMat.setDiffuseMap(diffuseMap);
        guiMat.setBumpMap(bumpMap);
        guiOb.setMaterial(guiMat);


        guiOb.setOnMouseEntered(new EventHandler<MouseEvent> () {
            @Override
            public void handle(MouseEvent t) {
                Vector3D coordinate = object.getCentralPos();
                //limit decimals to 3 points
                float sX = ((int) (coordinate.getX() * 1000)) / 1000F;
                float sY = ((int) (coordinate.getY() * 1000)) / 1000F;
                float sZ = ((int) (coordinate.getZ() * 1000)) / 1000F;

                identifierLabel.setText(object.getName() + ":\nX: " + sX + "\nY: " + sY + "\nZ: " + sZ);
            }
        });
        guiOb.setOnMouseExited(e -> identifierLabel.setText(""));

        guiOb.setOnMouseClicked(new EventHandler<MouseEvent> () {
            @Override
            public void handle(MouseEvent t) {
                followObject = object;
            }
        });

        return guiOb;
    }
    public Group loadRocket() {
        File file = new File(FILE_LOC);
        ObjModelImporter objImporter = new ObjModelImporter();
        TdsModelImporter tdsImporter = new TdsModelImporter();
        String filename = "";
        try {
            filename = file.toURI().toURL().toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        System.out.println(filename);
        objImporter.read(filename);
        final Node[] tdsMesh = (Node[]) objImporter.getImport();

        Map<String, PhongMaterial> mapTexs = objImporter.getNamedMaterials();
        Iterator<String> it = mapTexs.keySet().iterator();
        boolean differCol = true;
        while (it.hasNext()) {
            String key = it.next();
            if (differCol) mapTexs.get(key).setDiffuseColor(Color.RED);
            else mapTexs.get(key).setDiffuseColor(Color.WHITE);

            differCol = !differCol;
        }

        objImporter.close();

        Group rocketGroup = new Group();
        for (int i = 1; i < tdsMesh.length; i++) {
            rocketGroup.getChildren().add(tdsMesh[i]);
        }
        return rocketGroup;
    }

    private void updateGUIobject(Sphere guiObject, CelestialObject cObject){
        Vector3D coordinate = cObject.getCentralPos();
        coordinate = coordinate.substract(followObject.getCentralPos());
        guiObject.setRadius(cObject.getRadius() /1000 * plntRadFact);
        guiObject.setTranslateX(coordinate.getX() * DistanceMultiplier / MathUtil.AU);
        guiObject.setTranslateY(coordinate.getY() * DistanceMultiplier * -1 / MathUtil.AU);
        guiObject.setTranslateZ(coordinate.getZ() * DistanceMultiplier / MathUtil.AU);
        guiObject.setRotate(guiObject.getRotate() + 20);
    }
    private void updateRocket(Group guiObject, CelestialObject cObject){
        Vector3D coordinate = cObject.getCentralPos();
        coordinate = coordinate.substract(followObject.getCentralPos());
        guiObject.setTranslateX(coordinate.getX() * DistanceMultiplier / MathUtil.AU);
        guiObject.setTranslateY(coordinate.getY() * DistanceMultiplier * -1 / MathUtil.AU);
        guiObject.setTranslateZ(coordinate.getZ() * DistanceMultiplier / MathUtil.AU);
    }

    private void updateRadius(Sphere guiObject, CelestialObject cObject) {
        guiObject.setRadius(cObject.getRadius()/1000 * plntRadFact);
    }

    private SolarSystem toTitanTrajectory() throws IOException {
        Date departDate = new Date(2018, 0, 18);
        Date refDate = new Date(departDate);
        SolarSystem sol_depart = new SolarSystem();
        sol_depart.setPositionsPlanetsAtDateKepler(departDate);

        Date flybyDate = new Date(2020, 2, 12);
        Date arrivalDate = new Date(2026, 9, 10);
        pauseDate = new Date(arrivalDate);
        SolarSystem sol_reference = new SolarSystem();
        sol_reference.setPositionsPlanetsAtDateKepler(refDate);

        double tof = (arrivalDate.getTimeInMillis() - departDate.getTimeInMillis()) / 1000D;
        double tof_flyby = (flybyDate.getTimeInMillis() - departDate.getTimeInMillis()) / 1000D;
        long timestep_seconds = 30;
//        Vector3D flybyPos = null;
//        Vector3D flybyVel = null;
//
//        sol_reference.initializeAnimationWithPlanets(refDate, null);
//        for(double tof_cur = 0; tof_cur < tof; tof_cur = tof_cur + timestep_seconds){
//            sol_reference.updateAnimation(timestep_seconds, TimeUnit.SECONDS);
//            if(flybyPos == null && tof_cur > tof_flyby){
//                flybyPos = sol_reference.getPlanets().getJupiter().getCentralPos();
//                flybyVel = sol_reference.getPlanets().getJupiter().getCentralVel();
//            }
//        }

        Planet tit_arr = sol_reference.getPlanets().getTitan();
        Planet tit_dep = sol_depart.getPlanets().getTitan();
        Planet earth_dep = sol_depart.getPlanets().getEarth();
        Planet jupiter_dep = sol_depart.getPlanets().getJupiter();
        //double mass, Planet fromPlanet, Planet toPlanet, Date current_date, Vector3D departurePos, Vector3D destinationPos, Date arrivalDate ;

        Vector3D arrivalPos = new Vector3D(1.3735128425635854E12, 2.792784080558467E11, -6.002744211162953E10);
//        Vector3D arrivalPos =        tit_arr.getCentralPos();

        Vector3D flybyPos = new Vector3D(1.5715795415164874E11, -7.625355088251364E11, -3.49797418664907E8);
        Vector3D flybyVel = new Vector3D(12642.297961295031, 3251.850250468635, -296.43196830395976);
        double sphereOfInfluenceJup = sol_reference.getPlanets().getJupiter().getSphereOfInfluence();
        flybyPos = flybyPos.add(flybyVel.unit().scale(sphereOfInfluenceJup * - 0.8));
        Vector3D startVel = earth_dep.getCentralVel();
        //Planet fromPlanet, Planet Jupiter, Planet toPlanet, Date current_date, Date jupiter_date, Date arrival_date, Vector3D departurePos, Vector3D flybyPos, Vector3D arrivalPos
        InterPlanetaryRocketToTitanFlyByJupiter rocket = new InterPlanetaryRocketToTitanFlyByJupiter(earth_dep, jupiter_dep, tit_dep, departDate, flybyDate, arrivalDate, null, flybyPos, arrivalPos);

        ArrayList<Projectile> proj = new ArrayList<>();
        proj.add(rocket);
        sol_depart.setODEsolver(odEsolver);
        sol_depart.initializeAnimationWithPlanets(departDate, proj);
        return sol_depart;
    }

    private SolarSystem fromTitanTrajectory() throws IOException {
        Date departDate = new Date(2028, 9, 12);
        Date refDate = new Date(departDate);
        SolarSystem sol_depart = new SolarSystem();
        sol_depart.setPositionsPlanetsAtDateKepler(departDate);

        Date arrivalDate = new Date(2034, 4, 22);
        pauseDate = new Date(arrivalDate);
        SolarSystem sol_reference = new SolarSystem();
        sol_reference.setPositionsPlanetsAtDateKepler(refDate);

        double tof = (arrivalDate.getTimeInMillis() - departDate.getTimeInMillis()) / 1000D;

        long timestep_seconds = 30;
        sol_reference.initializeAnimationWithPlanets(refDate, null);
        for(double tof_left = tof; tof_left > 0; tof_left = tof_left - timestep_seconds){
            sol_reference.updateAnimation(timestep_seconds, TimeUnit.SECONDS);
        }

        Planet eart_ref = sol_reference.getPlanets().getEarth();
        Planet eartDep = sol_depart.getPlanets().getEarth();
        Planet titArr = sol_depart.getPlanets().getSaturn();
        //double mass, Planet fromPlanet, Planet toPlanet, Date current_date, Vector3D departurePos, Vector3D destinationPos, Date arrivalDate ;

        Vector3D arrivalPos = eart_ref.getCentralPos();
        Vector3D startVel = titArr.getCentralVel();
//        double escapeVel_val = 11000;
//        Vector3D escapeVel = startVel.unit().scale(11000);
////        startVel = startVel.add(escapeVel);
//        double mass, Planet fromPlanet, Planet toPlanet, Date current_date, Vector3D departurePos, Vector3D departureVel, Vector3D destinationPos, Date arrivalDate
        InterPlanetaryRocketToEarth rocket = new InterPlanetaryRocketToEarth(1000, titArr, eartDep, departDate, null, startVel, arrivalPos, arrivalDate);
        ArrayList<Projectile> proj = new ArrayList<>();
        proj.add(rocket);
//
        sol_depart.initializeAnimationWithPlanets(departDate, proj);
        return sol_depart;
    }

}

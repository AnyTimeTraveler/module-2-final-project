package ss.project.foreign.ui;

import com.sun.j3d.utils.universe.SimpleUniverse;

import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WorldViewer extends Applet implements ActionListener {

	private final int width = 200;
	private final int height = 200;

	Canvas3D canvas3D;
	Positions positions;
	Panel c_container;

	private SimpleUniverse universe = null;

	public WorldViewer() {
		//TODO: reference the World to get the data.
		this.positions = new Positions();

		//Create a 3D graphics canvas.
		canvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
		canvas3D.setSize(width, height);
		canvas3D.setLocation(5, 5);

		// Create the scene branchgroup.
		BranchGroup scene3D = createScene3D();

		// Create a universe with the Java3D universe utility.
		universe = new SimpleUniverse(canvas3D);
		universe.addBranchGraph(scene3D);

		//Use parallel projection
		View view = universe.getViewer().getView();
		view.setProjectionPolicy(View.PARALLEL_PROJECTION);

		//Set the universe Transform3D object.
		TransformGroup tg = universe.getViewingPlatform().getViewPlatformTransform();
		Transform3D transform = new Transform3D();
		transform.set(65.f, new Vector3f(0.0f, 0.0f, 400.0f));
		tg.setTransform(transform);

		// Create the canvas container.
		c_container = new Panel();
		c_container.setSize(720, 360);
		c_container.setLocation(0, 0);
		c_container.setVisible(true);
		c_container.setLayout(null);
		add(c_container);

		// Add the 2D and 3D canvases to the container.
		c_container.add(canvas3D);
	}

	public void destroy() {
		universe.cleanup();
	}

	/**
	 * Create the scenegraph for the 3D view.
	 */
	public BranchGroup createScene3D() {

		// Define colors
		Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
		Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
		Color3f red = new Color3f(0.80f, 0.20f, 0.2f);
		Color3f ambient = new Color3f(0.25f, 0.25f, 0.25f);
		Color3f diffuse = new Color3f(0.7f, 0.7f, 0.7f);
		Color3f specular = new Color3f(0.9f, 0.9f, 0.9f);
		Color3f ambientRed = new Color3f(0.2f, 0.05f, 0.0f);
		Color3f bgColor = new Color3f(0.05f, 0.05f, 0.2f);

		// Create the branch group
		BranchGroup branchGroup = new BranchGroup();

		// Create the bounding leaf node
		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0);
		BoundingLeaf boundingLeaf = new BoundingLeaf(bounds);
		branchGroup.addChild(boundingLeaf);

		// Create the background
		Background bg = new Background(bgColor);
		bg.setApplicationBounds(bounds);
		branchGroup.addChild(bg);

		// Create the ambient light
		AmbientLight ambLight = new AmbientLight(white);
		ambLight.setInfluencingBounds(bounds);
		branchGroup.addChild(ambLight);

		// Create the directional light
		Vector3f dir = new Vector3f(-1.0f, -1.0f, -1.0f);
		DirectionalLight dirLight = new DirectionalLight(white, dir);
		dirLight.setInfluencingBounds(bounds);
		branchGroup.addChild(dirLight);

		// Create the pole appearance
		Material poleMaterial = new Material(ambient, black, diffuse, specular, 110.f);
		poleMaterial.setLightingEnable(true);
		Appearance poleAppearance = new Appearance();
		poleAppearance.setMaterial(poleMaterial);

		// Create the transform group node
		TransformGroup transformGroup = new TransformGroup();
		transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		branchGroup.addChild(transformGroup);

		// Create the poles
		Poles poles = new Poles(poleAppearance);
		transformGroup.addChild(poles.getChild());

		// Add the position markers to the transform group
		transformGroup.addChild(positions.getChild());

		// Let the positions object know about the transform group
		positions.setTransformGroup(transformGroup);

		// Create the mouse pick and drag behavior node
		PickDragBehavior2 behavior = new PickDragBehavior2(canvas3D, positions, branchGroup, transformGroup);
		behavior.setSchedulingBounds(bounds);
		transformGroup.addChild(behavior);

		return branchGroup;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Object target = event.getSource();

		//Do UI stuff.
	}
}

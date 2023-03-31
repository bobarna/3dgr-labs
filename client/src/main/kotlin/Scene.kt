import org.w3c.dom.HTMLCanvasElement
import org.khronos.webgl.WebGLRenderingContext as GL //# GL# we need this for the constants declared ˙HUN˙ a constansok miatt kell
import kotlin.js.Date
import vision.gears.webglmath.UniformProvider
import vision.gears.webglmath.Vec1
import vision.gears.webglmath.Vec2
import vision.gears.webglmath.Vec3
import vision.gears.webglmath.Vec4
import vision.gears.webglmath.Mat4
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.cos

class Scene (
  val gl : WebGL2RenderingContext)  : UniformProvider("scene") {

  val vsTextured = Shader(gl, GL.VERTEX_SHADER, "textured-vs.glsl")
  val fsTextured = Shader(gl, GL.FRAGMENT_SHADER, "textured-fs.glsl")
  val texturedProgram = Program(gl, vsTextured, fsTextured)

  val texturedQuadGeometry = TexturedQuadGeometry(gl)

  // LABTODO: load geometries from the JSON file, create Meshes  

  val gameObjects = ArrayList<GameObject>()

  init {
    // LABTODO: create and add game object using meshes loaded from JSON
  }

  // LABTODO: replace with 3D camera
  val camera = OrthoCamera(*Program.all).apply{
    position.set(1f, 1f)
    windowSize.set(20f, 20f)
    updateViewProjMatrix()
  }

  fun resize(canvas : HTMLCanvasElement) {
    gl.viewport(0, 0, canvas.width, canvas.height)//#viewport# tell the rasterizer which part of the canvas to draw to ˙HUN˙ a raszterizáló ide rajzoljon
    camera.setAspectRatio(canvas.width.toFloat()/canvas.height)
  }

  val timeAtFirstFrame = Date().getTime()
  var timeAtLastFrame =  timeAtFirstFrame

  init{
    //LABTODO: enable depth test
    addComponentsAndGatherUniforms(*Program.all)
  }

  @Suppress("UNUSED_PARAMETER")
  fun update(keysPressed : Set<String>) {
    val timeAtThisFrame = Date().getTime() 
    val dt = (timeAtThisFrame - timeAtLastFrame).toFloat() / 1000.0f
    val t = (timeAtThisFrame - timeAtFirstFrame).toFloat() / 1000.0f
    timeAtLastFrame = timeAtThisFrame

    //LABTODO: move camera
    
    gl.clearColor(0.3f, 0.0f, 0.3f, 1.0f)//## red, green, blue, alpha in [0, 1]
    gl.clearDepth(1.0f)//## will be useful in 3D ˙HUN˙ 3D-ben lesz hasznos
    gl.clear(GL.COLOR_BUFFER_BIT or GL.DEPTH_BUFFER_BIT)//#or# bitwise OR of flags

    gl.enable(GL.BLEND)
    gl.blendFunc(
      GL.SRC_ALPHA,
      GL.ONE_MINUS_SRC_ALPHA)

    gameObjects.forEach{ it.move(dt, t, keysPressed, gameObjects) }

    gameObjects.forEach{ it.update() }
    gameObjects.forEach{ it.draw(this, camera) }
  }
}

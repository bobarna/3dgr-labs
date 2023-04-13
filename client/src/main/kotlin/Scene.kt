import org.w3c.dom.HTMLCanvasElement
import org.khronos.webgl.WebGLRenderingContext as GL
import org.khronos.webgl.Float32Array
import vision.gears.webglmath.UniformProvider
import vision.gears.webglmath.Vec3
import vision.gears.webglmath.Mat4
import kotlin.js.Date

class Scene (
  val gl : WebGL2RenderingContext) : UniformProvider("scene") {

  val vsTrafo = Shader(gl, GL.VERTEX_SHADER, "trafo-vs.glsl")
  val fsTextured = Shader(gl, GL.FRAGMENT_SHADER, "textured-fs.glsl")
  val fsEnvmapped = Shader(gl, GL.FRAGMENT_SHADER, "envmapped-fs.glsl")
  val fsMaxBlinn = Shader(gl, GL.FRAGMENT_SHADER, "maxblinn-fs.glsl")   
  val texturedProgram = Program(gl, vsTrafo, fsTextured)
  val envmappedProgram = Program(gl, vsTrafo, fsEnvmapped)
  val maxBlinnProgram = Program(gl, vsTrafo, fsMaxBlinn)  
  val vsQuad = Shader(gl, GL.VERTEX_SHADER, "quad-vs.glsl")
  val fsBackground = Shader(gl, GL.FRAGMENT_SHADER, "background-fs.glsl")
  val backgroundProgram = Program(gl, vsQuad, fsBackground)
  val backgroundMaterial = Material(backgroundProgram)
  val skyCubeTexture = TextureCube(gl,
      "media/posx512.jpg", "media/negx512.jpg",
      "media/posy512.jpg", "media/negy512.jpg",
      "media/posz512.jpg", "media/negz512.jpg"
    )
  init {
    backgroundMaterial["envTexture"]?.set( skyCubeTexture )
  }
  val quadGeometry = TexturedQuadGeometry(gl)
  val backgroundMesh = Mesh(backgroundMaterial, quadGeometry)

  val slowpokeTexture = Texture2D(gl, "media/slowpoke/YadonDh.png")
  val slowpokeEyeTexture = Texture2D(gl, "media/slowpoke/YadonEyeDh.png")  
  val slowpokeMaterial = Material(texturedProgram)
  init{
    slowpokeMaterial["colorTexture"]?.set(slowpokeTexture)
  }
  val slowpokeEyeMaterial = Material(texturedProgram)  
  init{
    slowpokeEyeMaterial["colorTexture"]?.set(slowpokeEyeTexture)
  }
  val jsonLoader = JsonLoader()
  val slowpokeGeometries = jsonLoader.loadGeometries(gl,
    "media/slowpoke/slowpoke.json")
  val slowpokeMeshes = arrayOf(
    Mesh(
      Material(texturedProgram).apply{
        this["colorTexture"]?.set(
          Texture2D(gl, "media/slowpoke/YadonDh.png"))
      }, slowpokeGeometries[0]),
    Mesh(
      Material(texturedProgram).apply{
        this["colorTexture"]?.set(
          Texture2D(gl, "media/slowpoke/YadonEyeDh.png"))
      }, slowpokeGeometries[1]),
  )

  val envmappedMaterial = Material(envmappedProgram)
  init{
    //LABTODO: set environment to env mapped material
  }
  val envmappedSlowpokeMeshes = arrayOf( 
    Mesh(envmappedMaterial, slowpokeGeometries[0]),
    Mesh(envmappedMaterial, slowpokeGeometries[1])
  )
  val shadedSlowpokeMaterial = Material(maxBlinnProgram)
  init{
    //LABTODO: set surface texture to shading material
  }
  val shadedSlowpokeEyeMaterial = Material(maxBlinnProgram)  
  init{
    //LABTODO: set surface texture to shading material
  }  
  val shadedSlowpokeMeshes = arrayOf( 
    Mesh(shadedSlowpokeMaterial, slowpokeGeometries[0]),
    Mesh(shadedSlowpokeEyeMaterial, slowpokeGeometries[1])
  )

  val gameObjects = ArrayList<GameObject>()
  val avatar = GameObject(*slowpokeMeshes)
  init{
    gameObjects.add(avatar)
    val envmappedObject = GameObject(*envmappedSlowpokeMeshes).apply{
      position.set(10.0f)
    }
    gameObjects += envmappedObject
    val shadedObject = GameObject(*shadedSlowpokeMeshes).apply{
      position.set(-10.0f)
    }
    gameObjects += shadedObject    
  }

  val flipQuadGeometry = FlipQuadGeometry(gl)
  //LABTODO: resources for multipass rendering

  //LABTODO: lights

  val camera = PerspectiveCamera(*Program.all)

  val timeAtFirstFrame = Date().getTime()
  var timeAtLastFrame =  timeAtFirstFrame

  init{
    gl.enable(GL.DEPTH_TEST)
    addComponentsAndGatherUniforms(*Program.all)
  }


  fun resize(gl : WebGL2RenderingContext, canvas : HTMLCanvasElement) {
    gl.viewport(0, 0, canvas.width, canvas.height)
    camera.setAspectRatio(canvas.width.toFloat() / canvas.height.toFloat())

    //LABTODO: create and bind framebuffer resources
  }

  fun update(gl : WebGL2RenderingContext, keysPressed : Set<String>) {

    val timeAtThisFrame = Date().getTime() 
    val dt = (timeAtThisFrame - timeAtLastFrame).toFloat() / 1000.0f
    val t  = (timeAtThisFrame - timeAtFirstFrame).toFloat() / 1000.0f    
    timeAtLastFrame = timeAtThisFrame

    camera.move(dt, keysPressed)

    //LABTODO: set render target

    // clear the screen
    gl.clearColor(0.3f, 0.0f, 0.3f, 1.0f)
    gl.clearDepth(1.0f)
    gl.clear(GL.COLOR_BUFFER_BIT or GL.DEPTH_BUFFER_BIT)

    val spawn = ArrayList<GameObject>()
    val killList = ArrayList<GameObject>()    
    gameObjects.forEach { 
      if(!it.move(t, dt, keysPressed, gameObjects, spawn)){
        killList.add(it)
      }
    }
    killList.forEach{ gameObjects.remove(it) }
    spawn.forEach{ gameObjects.add(it) }

    gameObjects.forEach { it.update() }

    backgroundMesh.draw(camera)
    gameObjects.forEach { it.draw( camera /* LABTODO: pass lights*/) }

    //LABTODO: post processing
  }
}

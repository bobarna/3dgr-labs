import org.w3c.dom.HTMLCanvasElement
import org.khronos.webgl.WebGLRenderingContext as GL
import vision.gears.webglmath.*
import kotlin.js.Date
import kotlin.math.*

class Scene (
  val gl : WebGL2RenderingContext) : UniformProvider("scene") {

  val vsTrafo = Shader(gl, GL.VERTEX_SHADER, "trafo-vs.glsl")
  val fsTextured = Shader(gl, GL.FRAGMENT_SHADER, "textured-fs.glsl")
  val texturedProgram = Program(gl, vsTrafo, fsTextured)

  val quadGeometry = TexturedQuadGeometry(gl)

  val timeAtFirstFrame = Date().getTime()
  var timeAtLastFrame =  timeAtFirstFrame

  fun resize(gl : WebGL2RenderingContext, canvas : HTMLCanvasElement) {
    gl.viewport(0, 0, canvas.width, canvas.height)
    //LABTODO: set camera aspect ratio
  }

  @Suppress("UNUSED_PARAMETER")
  fun update(gl : WebGL2RenderingContext, keysPressed : Set<String>) {

    val timeAtThisFrame = Date().getTime() 
    val dt = (timeAtThisFrame - timeAtLastFrame).toFloat() / 1000.0f
    val t  = (timeAtThisFrame - timeAtFirstFrame).toFloat() / 1000.0f    
    timeAtLastFrame = timeAtThisFrame

    gl.enable(GL.BLEND)
    gl.blendFunc(GL.SRC_ALPHA, GL.ONE_MINUS_SRC_ALPHA)

    // clear the screen
    gl.clearColor(0.2f, 0.0f, 0.5f, 1.0f)
    gl.clearDepth(1.0f)
    gl.clear(GL.COLOR_BUFFER_BIT or GL.DEPTH_BUFFER_BIT)

    gl.useProgram(texturedProgram.glProgram)
    val modelMatrix = Mat4().scale(0.1f, 0.1f, 0.1f)
    modelMatrix.commit(
      gl,
      gl.getUniformLocation(texturedProgram.glProgram, "gameObject.modelMatrix") ?: throw Error("No modelMatrix!")
    )
    quadGeometry.draw()
  }
}

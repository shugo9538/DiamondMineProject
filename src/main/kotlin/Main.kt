import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*
import kotlin.math.*

const val MAX = 751
var row: Int = 0
var col: Int = 0
lateinit var mine: Array<IntArray>
lateinit var NW: Array<IntArray>
lateinit var NE: Array<IntArray>
lateinit var SW: Array<IntArray>
lateinit var SE: Array<IntArray>

fun init(){
    mine = Array(MAX){IntArray(MAX)}
    SW = Array(MAX){IntArray(MAX)}
    NE = Array(MAX){IntArray(MAX)}
    NW = Array(MAX){IntArray(MAX)}
    SE = Array(MAX){IntArray(MAX)}
}

fun isInRange(r: Int, c: Int): Boolean {
    if( r < 0 || r >= row || c < 0 || c >= col ) return false
    return true
}

// 한 점에서 이어지는 최대 변 길이
fun maximumEdge() {
    for(i in 1 until row+col-1) {
        for (c in 0 until col) {
            val r = i-c
            if (!isInRange(r, c)) continue
            if (!isInRange(r + 1, c - 1)) SW[r][c] = mine[r][c]
            else SW[r][c] = mine[r][c] * (SW[r + 1][c - 1] + 1)
        }
        for (r in 0 until row) {
            val c = i - r
            if (!isInRange(r, c)) continue
            if (!isInRange(r - 1, c + 1)) NE[r][c] = mine[r][c]
            else NE[r][c] = mine[r][c] * (NE[r - 1][c + 1] + 1)
        }
    }
    for (i in 1-col until row) {
        for (r in 0 until row) {
            val c = r - i
            if (!isInRange(r, c)) continue
            if (!isInRange(r - 1, c - 1)) NW[r][c] = mine[r][c]
            else NW[r][c] = mine[r][c] * (NW[r - 1][c - 1] + 1)
        }

        for (r in row-1 downTo 0) {
            val c = r - i
            if (!isInRange(r, c)) continue
            if (!isInRange(r + 1, c + 1)) SE[r][c] = mine[r][c]
            else SE[r][c] = mine[r][c] * (SE[r + 1][c + 1] + 1)
        }
    }
}

// 위에서 구한 변 길이를 바탕으로 다이아 만들어지는지 확인
fun checkDiamond(): Int {
    var result: Int = 0
    for(r in 0 until row) {
        for(c in 0 until col) {
            val commonMaxSize = min(NE[r][c], SE[r][c])
            if(commonMaxSize < result) continue

            for(i in commonMaxSize downTo 1){
                val nextC = c+2*(i-1)

                if (nextC >= col) continue
                if (i < result) break
                if(min(SW[r][nextC], NW[r][nextC]) >= i){
                    result = max(result, i)
                    break
                }
            }
        }
    }
    return result
}

fun main() {
    val br = BufferedReader(InputStreamReader(System.`in`))
    val st = StringTokenizer(br.readLine())
    var input: String
    var dia: Int
    var max = 0
    
    row = st.nextToken().toInt()
    col = st.nextToken().toInt()

    init()

    for (i in 0 until row) {
        input = br.readLine().toString()
        for (j in 0 until col) {
            dia = Integer.parseInt(input.get(j).toString())
            mine[i][j] = dia
        }
    }

    if(row == 1 && col == 1) {
        println(mine[0][0])
        return
    }

    maximumEdge()
    max = checkDiamond()

    println(max)
}
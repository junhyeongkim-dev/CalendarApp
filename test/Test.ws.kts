import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale

fun getDayOfWeek(year: Int, month: Int): String {
    // 해당 연도와 월의 첫 번째 날짜를 LocalDate 객체로 생성합니다.
    val date = LocalDate.of(year, month, 1)
    // 해당 날짜의 요일을 얻습니다.
    return date.dayOfWeek.name

}

fun getPreviousMonth(year: Int, month: Int): Pair<Int, Int> {
    // 입력받은 년과 월로 LocalDate 객체를 생성합니다.
    val currentDate = LocalDate.of(year, month, 1)

    // minusMonths 메소드를 사용하여 한 달을 빼줍니다.
    val previousMonthDate = currentDate.minusMonths(1)

    // 이전 달의 년도와 월을 Pair로 묶어 반환합니다.
    return Pair(previousMonthDate.year, previousMonthDate.monthValue)
}

fun getCurrentYearMonth() : String {
    val currentDate = LocalDate.now()

    val year = currentDate.year
    val month = currentDate.monthValue

    month.toString().padStart(2, '0')
    return "${year}-${month}"
}

fun getIntervalMonth() {
    // 시작 날짜와 종료 날짜를 YearMonth 인스턴스로 생성
    val start = YearMonth.of(2024, 2)
    val end = YearMonth.of(2024, 6)

    // ChronoUnit.MONTHS.between() 메서드를 사용하여 두 날짜 간의 월 차이를 계산
    val monthsBetween = ChronoUnit.MONTHS.between(start, end)

    println("두 날짜의 월 차이: $monthsBetween 개월")
}

fun flatMapTest() {
    val fruits = listOf("apple", "banana")
    val characters = fruits.flatMap {
        it.toList()
    }

    println(characters)
}

//getDayOfWeek(2024, 2)
//getPreviousMonth(2024, 3)
//getCurrentYearMonth()

//getIntervalMonth()
flatMapTest()

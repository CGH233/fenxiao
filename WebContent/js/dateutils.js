
function getDayStartDate(date){
	var year = date.getFullYear()
	var month = date.getMonth()
	var day = date.getDate()
	var dayStartDate = new Date(year, month, day,0,0,0)
	return dayStartDate
} 
function getDayEndDate(date){
	var year = date.getFullYear()
	var month = date.getMonth()
	var day = date.getDate()
	var dayEndDate = new Date(year, month, day,23,59,59)
	return dayEndDate
} 
function getMonthStartDate(date) {
	date.setDate(1)
    var monthStartDate = getDayStartDate(date)
    return monthStartDate
  }
function getMonthEndDate(date) {
	var lastMonthStartDate = new Date(date.getFullYear(), date.getMonth()+1, 0,0,0,0);
	var monthEndDate = getDayEndDate(date)
    return monthEndDate
}
function getLastMonthStartDate(date) {
	var lastMonthStartDate = new Date(date.getFullYear(), date.getMonth()-1, 1,0,0,0);
	return lastMonthStartDate
}
function getLastMonthEndDate(date) {
	date.setDate(0)
	var lastMonthEndDate = getDayEndDate(date)
    return lastMonthEndDate
}
function formatDate(date){
    var seperator1 = "-";
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var formatdate = year + seperator1 + month + seperator1 + strDate;
    return formatdate;
}
  
/**
 * 日志页面的js
 */

function changePerPage()
{
	var perPageCount=$("#perPageCountSelect").val();
	window.location.href="clientPageAction?perPageCountClient="+perPageCount;
	



}
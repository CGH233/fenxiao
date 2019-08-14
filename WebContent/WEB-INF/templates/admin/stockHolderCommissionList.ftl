<form id="pagerForm" onsubmit="return navTabSearch(this);" action="userList.do" method="post">
        <input type="hidden" name="pageCurrent" value="${page.pageCurrent}">
        <input type="hidden" name="pageSize" value="${page.pageSize}">
</form>



<div class="bjui-pageContent">
<form action="stockholderPay" class="pageForm" data-toggle="validate" data-reload-navtab="true">
    <table data-toggle="tablefixed" data-width="100%" data-layout-h="0" data-nowrap="true">
        <thead>
			<tr>
				<th orderField="code">用户名</th>
				<th orderField="name">身份</th>
                <th orderField="name">股东分红奖金额(元)</th>               
			</tr>
		</thead>
		<tbody>
        <#list userList as user>
        	<#if user.commission != 0>
            <tr>
                <td>${user.name}</td>
                <td>
                	<#list bounsRuleList as identity>
                		<#if user.level == identity.level>${identity.identityName}</#if>
                	</#list>
                	<#if user.level == 0>无身份</#if>
                </td>
                <td>${user.commission}</td>              
            </tr>
            </#if>
		</#list>
		</tbody>
    	<div class="bjui-footBar" align="left">
           <button type="submit" class="btn-default">提交</button>  		
        </div>
    	</form>
    </table>
    
    <#include "pageBar.ftl"/>
</div>
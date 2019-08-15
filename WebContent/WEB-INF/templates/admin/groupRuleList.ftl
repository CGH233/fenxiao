<form id="pagerForm" onsubmit="return navTabSearch(this);" action="userList.do" method="post">
        <input type="hidden" name="pageCurrent" value="${page.pageCurrent}">
        <input type="hidden" name="pageSize" value="${page.pageSize}">
</form>

<div class="bjui-pageContent">
    <table data-toggle="tablefixed" data-width="100%" data-layout-h="0" data-nowrap="true">
        <thead>
			<tr>
				<th orderField="code">序号</th>
				<th orderField="name">达标下限(万)</th>
                <th orderField="name">达标上限(万)</th>
                <th orderField="name"> 提成(元)</th>
                <th orderField="grade">身份需求</th>               
                <th width="90">操作</th>
			</tr>
		</thead>
		<tbody>
        <#list groupRuleList as groupRule>
        <#if groupRule.level == 0 || groupRule.level == 6>
            <tr>
                <td>${groupRule.id}</td>
                <td>${groupRule.lower_limit}</td>
                <td>${groupRule.upper_limit!"无上限"}</td>
                <td>${groupRule.commission}</td>
                <td><#if groupRule.level == 6 >联创股东<#else>区代以上</#if></td>
                <td>
                    <a href="groupRuleEdit.action?id=${groupRule.id}" class="btn btn-green" data-toggle="dialog" data-width="800" data-height="400" data-id="userEdit" data-mask="true">编辑</a>
                   
                </td>
            </tr>
            </#if>
		</#list>
		</tbody>
    </table>
    <#include "pageBar.ftl"/>
</div>
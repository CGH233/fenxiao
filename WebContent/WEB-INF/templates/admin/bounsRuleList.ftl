<form id="pagerForm" onsubmit="return navTabSearch(this);" action="userList.do" method="post">
        <input type="hidden" name="pageCurrent" value="${page.pageCurrent}">
        <input type="hidden" name="pageSize" value="${page.pageSize}">
</form>

<div class="bjui-pageContent">
    <table data-toggle="tablefixed" data-width="100%" data-layout-h="0" data-nowrap="true">
        <thead>
			<tr>
				<th orderField="code">身份名称</th>
				<th orderField="name">直推奖励</th>
                <th orderField="name">间推奖励</th>
                <th orderField="name">区域奖</th>
                <th orderField="grade">直接零售收益</th>
                <th orderField="idno">间接零售收益</th>
                <th orderField="idno">2级之后的管理奖</th>
                <th orderField="idno">直接零售收益（复购）</th>
                <th orderField="idno">间接零售收益（复购）</th>
                <th orderField="idno">2级之后的管理奖（复购）</th>
                <th width="90">操作</th>
			</tr>
		</thead>
		<tbody>
        <#list bounsRuleList as bounsRule>
            <tr>
                <td>${bounsRule.identityName}</td>
                <td>${bounsRule.directReward}</td>
                <td>${bounsRule.indirectReward}</td>
                <td>${bounsRule.regionalReward}</td>
                <td>${bounsRule.directRetail}</td>
                <td>${bounsRule.indirectRetail}</td>
                <td>${bounsRule.twoMaReward}</td>
                <td>${bounsRule.directRetailRe}</td>
                <td>${bounsRule.indirectRetailRe}</td>
                <td>${bounsRule.twoMaRetailRe}</td>
                <td>
                    <a href="bounsRuleEdit.action?level=${bounsRule.level?string('#')}" class="btn btn-green" data-toggle="dialog" data-width="800" data-height="400" data-id="userEdit" data-mask="true">编辑</a>
                   
                </td>
            </tr>
		</#list>
		</tbody>
    </table>
    <#include "pageBar.ftl"/>
</div>
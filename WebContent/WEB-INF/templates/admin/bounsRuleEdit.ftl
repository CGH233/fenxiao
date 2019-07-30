<div class="bjui-pageContent">
    <form action="bounsRuleUpdate" class="pageForm" data-toggle="validate" data-reload-navtab="true">
        <input type="hidden" name="bounsRule.level" value="${bounsRule.level?string('#')}">
        <div class="pageFormContent" data-layout-h="0">
            <table class="table table-condensed table-hover">
                <tbody>
                    <tr>
                        <td colspan="2" align="center"><h3>修改奖励机制</h3></td>
                    </tr>
                    <tr>
                    	<td>
                            <label for="j_dialog_code" class="control-label x90">身份名称：</label>
                            <input type="text" name="bounsRule.identityName" id="identityName" data-rule="required" size="20" value="${bounsRule.identityName}">
                        </td>
                    </tr>
                    <tr>
                    	<td>
                            <label for="j_dialog_code" class="control-label x90">直推奖励：</label>
                            <input type="text" name="bounsRule.directReward" id="directReward" data-rule="required" size="20" value="${bounsRule.directReward}">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="j_dialog_name" class="control-label x90">间推奖励：</label>
                            <input type="text" name="bounsRule.indirectReward" id="indirectReward" data-rule="required" size="20" value="${bounsRule.indirectReward}">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="j_dialog_name" class="control-label x90">区域奖：</label>
                            <input type="text" name="bounsRule.regionalReward"  id="regionalReward" data-rule="required" size="20" value="${bounsRule.regionalReward}">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="j_dialog_name" class="control-label x90">直接零售收益：</label>
                            <input type="text" name="bounsRule.directRetail"  id="directRetail" data-rule="required" size="20" value="${bounsRule.directRetail}">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="j_dialog_name" class="control-label x90">间接零售收益：</label>
                            <input type="text" name="bounsRule.indirectRetail"  id="indirectRetail" data-rule="required" size="20" value="${bounsRule.indirectRetail}">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="j_dialog_name" class="control-label x90">2级之后的管理奖</label>
                            <input type="text" name="bounsRule.twoMaReward"  id="twoMaReward" data-rule="required" size="20" value="${bounsRule.twoMaReward}">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="j_dialog_name" class="control-label x90">直接零售收益（复购）：</label>
                            <input type="text" name="bounsRule.directRetailRe"  id="directRetailRe" data-rule="required" size="20" value="${bounsRule.directRetailRe}">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="j_dialog_name" class="control-label x90">间接零售收益（复购）：</label>
                            <input type="text" name="bounsRule.indirectRetailRe"  id="indirectRetailRe" data-rule="required" size="20" value="${bounsRule.indirectRetailRe}">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="j_dialog_name" class="control-label x90">2级之后的管理奖（复购）</label>
                            <input type="text" name="bounsRule.twoMaRetailRe"  id="twoMaRetailRe" data-rule="" size="20" value="${bounsRule.twoMaRetailRe}">
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
        <div class="bjui-footBar">
            <ul>
                <li><button type="button" class="btn-close">关闭</button></li>
                <li><button type="submit" class="btn-default">保存</button></li>
            </ul>
        </div>
    </form>
</div>
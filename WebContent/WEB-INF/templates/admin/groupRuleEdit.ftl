<div class="bjui-pageContent">
    <form action="groupRuleUpdate" class="pageForm" data-toggle="validate" data-reload-navtab="true">
        <input type="hidden" name="groupRule.id" value="${groupRule.id}">
        <div class="pageFormContent" data-layout-h="0">
            <table class="table table-condensed table-hover">
                <tbody>
                    <tr>
                        <td colspan="2" align="center"><h3>修改奖励机制</h3></td>
                    </tr>             
                    <tr>
                    	<td>
                            <label for="j_dialog_code" class="control-label x90">达标下限：</label>
                            <input type="text" name="groupRule.lower_limit" id="lower_limit" data-rule="required" size="20" value="${groupRule.lower_limit}">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="j_dialog_name" class="control-label x90">达标上限：</label>
                            <input type="text" name="groupRule.upper_limit" id="upper_limit" data-rule="required" size="20" value="${groupRule.upper_limit!"无上限"}">*输入"无上限"则设置为无上限
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="j_dialog_name" class="control-label x90">提成：</label>
                            <input type="text" name="groupRule.commission"  id="commission" data-rule="required" size="20" value="${groupRule.commission}">
                        </td>
                    </tr>
                    <tr>
                        <td>
							<label for="status" class="control-label x90">身份需求：</label>
                            <select name="groupRule.level" id="level" style="text-align:left;text-align-last:left;width:90px;margin:0 auto;" >
                                 <option value='0' <#if groupRule?if_exists.level < 6>selected</#if>>无</option>
                                 <option value='6' <#if groupRule?if_exists.level == 6>selected</#if>>联创股东</option>
                            </select>
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
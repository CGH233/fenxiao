<div class="bjui-pageContent">
    <form action="userUpdate" class="pageForm" data-toggle="validate" data-reload-navtab="true">
        <input type="hidden" name="user.id" value="${user.id?string('#')}">
        <div class="pageFormContent" data-layout-h="0">
            <table class="table table-condensed table-hover">
                <tbody>
                    <tr>
                        <td colspan="2" align="center"><h3>修改用户</h3></td>
                    </tr>
                    <tr>
                    	<td>
                            <label for="j_dialog_code" class="control-label x90">编号：</label>
                            <input type="text" name="user.no" readonly="readonly" id="no" data-rule="required" size="20" value="${user.no}">
                        </td>
                    </tr>
                    <tr>
                    	<td>
                            <label for="j_dialog_code" class="control-label x90">用户名：</label>
                            <input type="text" name="user.name"  id="name" data-rule="required" size="20" value="${user.name}">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="j_dialog_name" class="control-label x90">密码：</label>
                            <input type="text" name="user.password" id="password" data-rule="" size="20">不修改则留空
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="j_dialog_name" class="control-label x90">手机号：</label>
                            <input type="text" name="user.phone"  id="phone" data-rule="required" size="20" value="${user.phone}">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="j_dialog_name" class="control-label x90">余额：</label>
                            <input type="text" name="balance" readonly="readonly" id="balance" data-rule="required" size="20" value="${user.balance}">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="j_dialog_name" class="control-label x90">佣金：</label>
                            <input type="text" name="commission" readonly="readonly" id="commission" data-rule="required" size="20" value="${user.commission}">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="j_dialog_name" class="control-label x90">注册时间：</label>
                            <input type="text" name="createDate" readonly="readonly" id="createDate" data-rule="required" size="20" value="${user.createDate}">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="j_dialog_name" class="control-label x90">注册IP：</label>
                            <input type="text" name="registerIp" readonly="readonly" id="registerIp" data-rule="required" value="${user.registerIp}">
                        </td>
                    </tr>
                    <tr>
                        <td>
							<label for="status" class="control-label x90">状态：</label>
                            <select name="user.status" id="status" style="text-align:left;text-align-last:left;width:90px;margin:0 auto;" >
                                 <option value='0' <#if user?if_exists.status == 0>selected</#if>>未激活</option>
                                 <option value='1' <#if user?if_exists.status == 1>selected</#if>>已激活</option>
                            </select>
                        </td>
                    </tr>
					<tr>
                        <td>
							<label for="identityName" class="control-label x90">用户身份：</label>
                            <select name="user.level" id="identityName" style="text-align:left;text-align-last:left;width:90px" >
                                <option value='0' <#if user?if_exists.level == 0></#if>>无身份</option> 
								<#list bounsRuleList as identity>
									<option value="${identity.level}" <#if user?if_exists.level == identity.level>selected</#if>>
										${identity.identityName}
									</option>
								</#list>                             
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="j_dialog_name" class="control-label x90">最后登录时间：</label>
                            <input type="text" name="lastLoginTime" readonly="readonly" id="lastLoginTime" data-rule="required" size="20" value="${user.lastLoginTime!''}">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="j_dialog_name" class="control-label x90">最后登录IP：</label>
                            <input type="text" name="lastLoginIp" readonly="readonly" id="lastLoginIp" data-rule="" size="20" value="${user.lastLoginIp!''}">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="j_dialog_name" class="control-label x90">登录次数：</label>
                            <input type="text" name="loginCount" readonly="readonly" id="loginCount" data-rule="" size="20" value="${user.loginCount!''}">
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
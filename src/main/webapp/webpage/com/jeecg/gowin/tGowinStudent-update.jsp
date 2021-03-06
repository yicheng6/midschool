<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>学生管理</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript">
  //编写自定义JS代码
  </script>
 </head>
 <body>
		<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="tGowinStudentController.do?doUpdate" >
					<input id="id" name="id" type="hidden" value="${tGowinStudentPage.id }"/>
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
					<tr>
						<td align="right">
							<label class="Validform_label">
								学号:
							</label>
						</td>
						<td class="value">
						     	 <input id="xh" name="xh" type="text" style="width: 150px" class="inputxt"  datatype="*"  ignore="checked"  value='${tGowinStudentPage.xh}'/>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">学号</label>
						</td>
						<td align="right">
							<label class="Validform_label">
								姓名:
							</label>
						</td>
						<td class="value">
						     	 <input id="xm" name="xm" type="text" style="width: 150px" class="inputxt"  datatype="*"  ignore="checked"  value='${tGowinStudentPage.xm}'/>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">姓名</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								身份证件号:
							</label>
						</td>
						<td class="value">
						     	 <input id="xfzjh" name="xfzjh" type="text" style="width: 150px" class="inputxt"  datatype="*"  ignore="checked"  value='${tGowinStudentPage.xfzjh}'/>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">身份证件号</label>
						</td>
						<td align="right">
							<label class="Validform_label">
								性别:
							</label>
						</td>
						<td class="value">
									<t:dictSelect field="xbm" type="list"  datatype="*"  typeGroupCode="xbm"   defaultVal="${tGowinStudentPage.xbm}" hasLabel="false"  title="性别" ></t:dictSelect>     
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">性别</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								出生日期:
							</label>
						</td>
						<td class="value">
									  <input id="csrq" name="csrq" type="text" style="width: 150px"  class="Wdate" onClick="WdatePicker()"  ignore="ignore" value='<fmt:formatDate value='${tGowinStudentPage.csrq}' type="date" pattern="yyyy-MM-dd"/>'/>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">出生日期</label>
						</td>
						<td align="right">
							<label class="Validform_label">
								籍贯:
							</label>
						</td>
						<td class="value">
						     	 <input id="jg" name="jg" type="text" style="width: 150px" class="inputxt"  datatype="*"  ignore="checked"  value='${tGowinStudentPage.jg}'/>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">籍贯</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								民族码:
							</label>
						</td>
						<td class="value">
						     	 <input id="mzm" name="mzm" type="text" style="width: 150px" class="inputxt"  datatype="*"  ignore="checked"  value='${tGowinStudentPage.mzm}'/>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">民族码</label>
						</td>
						<td align="right">
							<label class="Validform_label">
								婚姻状况:
							</label>
						</td>
						<td class="value">
									<t:dictSelect field="hyzkm" type="list"  datatype="*"  typeGroupCode="hyzkm"   defaultVal="${tGowinStudentPage.hyzkm}" hasLabel="false"  title="婚姻状况" ></t:dictSelect>     
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">婚姻状况</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								联系电话:
							</label>
						</td>
						<td class="value">
						     	 <input id="xslxdh" name="xslxdh" type="text" style="width: 150px" class="inputxt"  ignore="ignore"  value='${tGowinStudentPage.xslxdh}'/>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">联系电话</label>
						</td>
						<td align="right">
							<label class="Validform_label">
								专业:
							</label>
						</td>
						<td class="value">
						     	 <input id="major" name="major" type="text" style="width: 150px" class="inputxt"  datatype="*"  ignore="checked"  value='${tGowinStudentPage.major}'/>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">专业</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								年级:
							</label>
						</td>
						<td class="value">
						     	 <input id="grade" name="grade" type="text" style="width: 150px" class="inputxt"  datatype="*"  ignore="checked"  value='${tGowinStudentPage.grade}'/>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">年级</label>
						</td>
						<td align="right">
							<label class="Validform_label">
								班级:
							</label>
						</td>
						<td class="value">
									<t:dictSelect field="xzbdm" type="list"  datatype="*"  dictTable="t_gowin_class" dictField="xzbdm" dictText="xzbmc"   defaultVal="${tGowinStudentPage.xzbdm}" hasLabel="false"  title="班级" ></t:dictSelect>     
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">班级</label>
						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								系统账号:
							</label>
						</td>
						<td class="value">
									<t:dictSelect field="sysAccount" type="list"  dictTable="t_s_base_user" dictField="username" dictText="username"   defaultVal="${tGowinStudentPage.sysAccount}" hasLabel="false"  title="系统账号" ></t:dictSelect>     
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">系统账号</label>
						</td>
				<td align="right">
					<label class="Validform_label">
					</label>
				</td>
				<td class="value">
				</td>
					</tr>
				
			</table>
		</t:formvalid>
 </body>
  <script src = "webpage/com/jeecg/gowin/tGowinStudent.js"></script>		

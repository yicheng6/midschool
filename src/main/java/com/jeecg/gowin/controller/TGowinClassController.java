package com.jeecg.gowin.controller;
import com.jeecg.gowin.entity.TGowinClassEntity;
import com.jeecg.gowin.entity.TGowinTeacherEntity;
import com.jeecg.gowin.service.TGowinClassServiceI;
import com.jeecg.gowin.service.TGowinTeacherServiceI;

import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.common.TreeChildCount;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.pojo.base.TSRole;
import org.jeecgframework.web.system.pojo.base.TSRoleUser;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.core.util.MyBeanUtils;

import java.io.OutputStream;

import org.jeecgframework.core.util.BrowserUtils;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.TemplateExportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.vo.TemplateExcelConstants;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jeecgframework.core.util.ResourceUtil;

import java.io.IOException;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Map;
import java.util.HashMap;

import org.jeecgframework.core.util.ExceptionUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.jeecgframework.core.beanvalidator.BeanValidators;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import java.net.URI;

import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

/**   
 * @Title: Controller  
 * @Description: 班级管理
 * @author onlineGenerator
 * @date 2017-12-10 22:59:23
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/tGowinClassController")
public class TGowinClassController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TGowinClassController.class);

	@Autowired
	private TGowinClassServiceI tGowinClassService;
	@Autowired
	private TGowinTeacherServiceI tGowinTeacherService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;
	


	/**
	 * 班级管理列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		TSUser u = ResourceUtil.getSessionUser();
		List<TSRoleUser> rUsers = systemService.findByProperty(TSRoleUser.class, "TSUser.id", u.getId());
		request.setAttribute("auth", false);
		for (TSRoleUser ru : rUsers) {
			TSRole role = ru.getTSRole();
			// 管理员权限
			if (role.getId().equals("8a8ab0b246dc81120146dc8181870050")) {
				request.setAttribute("auth", true);
			}
		}
		return new ModelAndView("com/jeecg/gowin/tGowinClassList");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(TGowinClassEntity tGowinClass,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TGowinClassEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, tGowinClass, request.getParameterMap());
		try{
			//自定义追加查询条件
			TSUser u = ResourceUtil.getSessionUser();
			List<TSRoleUser> rUsers = systemService.findByProperty(TSRoleUser.class, "TSUser.id", u.getId());
			boolean isAdmin = false;
			for (TSRoleUser ru : rUsers) {
				TSRole role = ru.getTSRole();
				// 管理员权限
				if (role.getId().equals("8a8ab0b246dc81120146dc8181870050")) {
					isAdmin = true;
				}
			}
			if (!isAdmin) {
				List<TGowinTeacherEntity> teachers = tGowinTeacherService.findByProperty(TGowinTeacherEntity.class, "sysAccount", u.getUserName());
				if (teachers.size() > 0) {
					cq.eq("bzrgh", teachers.get(0).getGh());
				} else {
					cq.eq("bzrgh", "-999");
				}
			}
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.tGowinClassService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * 删除班级管理
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(TGowinClassEntity tGowinClass, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		tGowinClass = systemService.getEntity(TGowinClassEntity.class, tGowinClass.getId());
		message = "班级管理删除成功";
		try{
			tGowinClassService.delete(tGowinClass);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "班级管理删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除班级管理
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "班级管理删除成功";
		try{
			for(String id:ids.split(",")){
				TGowinClassEntity tGowinClass = systemService.getEntity(TGowinClassEntity.class, 
				id
				);
				tGowinClassService.delete(tGowinClass);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "班级管理删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加班级管理
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(TGowinClassEntity tGowinClass, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "班级管理添加成功";
		try{
			tGowinClassService.save(tGowinClass);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "班级管理添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新班级管理
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(TGowinClassEntity tGowinClass, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "班级管理更新成功";
		TGowinClassEntity t = tGowinClassService.get(TGowinClassEntity.class, tGowinClass.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(tGowinClass, t);
			tGowinClassService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "班级管理更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * 班级管理新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(TGowinClassEntity tGowinClass, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(tGowinClass.getId())) {
			tGowinClass = tGowinClassService.getEntity(TGowinClassEntity.class, tGowinClass.getId());
			req.setAttribute("tGowinClassPage", tGowinClass);
		}
		return new ModelAndView("com/jeecg/gowin/tGowinClass-add");
	}
	/**
	 * 班级管理编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(TGowinClassEntity tGowinClass, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(tGowinClass.getId())) {
			tGowinClass = tGowinClassService.getEntity(TGowinClassEntity.class, tGowinClass.getId());
			req.setAttribute("tGowinClassPage", tGowinClass);
		}
		return new ModelAndView("com/jeecg/gowin/tGowinClass-update");
	}
	
	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","tGowinClassController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(TGowinClassEntity tGowinClass,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(TGowinClassEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, tGowinClass, request.getParameterMap());
		List<TGowinClassEntity> tGowinClasss = this.tGowinClassService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"班级管理");
		modelMap.put(NormalExcelConstants.CLASS,TGowinClassEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("班级管理列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
			"导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST,tGowinClasss);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * 导出excel 使模板
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(TGowinClassEntity tGowinClass,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"班级管理");
    	modelMap.put(NormalExcelConstants.CLASS,TGowinClassEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("班级管理列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
    	"导出信息"));
    	modelMap.put(NormalExcelConstants.DATA_LIST,new ArrayList());
    	return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "importExcel", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson importExcel(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			MultipartFile file = entity.getValue();// 获取上传文件对象
			ImportParams params = new ImportParams();
			params.setTitleRows(2);
			params.setHeadRows(1);
			params.setNeedSave(true);
			try {
				List<TGowinClassEntity> listTGowinClassEntitys = ExcelImportUtil.importExcel(file.getInputStream(),TGowinClassEntity.class,params);
				for (TGowinClassEntity tGowinClass : listTGowinClassEntitys) {
					tGowinClassService.save(tGowinClass);
				}
				j.setMsg("文件导入成功！");
			} catch (Exception e) {
				j.setMsg("文件导入失败！");
				logger.error(ExceptionUtil.getExceptionMessage(e));
			}finally{
				try {
					file.getInputStream().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return j;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<TGowinClassEntity> list() {
		List<TGowinClassEntity> listTGowinClasss=tGowinClassService.getList(TGowinClassEntity.class);
		return listTGowinClasss;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		TGowinClassEntity task = tGowinClassService.get(TGowinClassEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody TGowinClassEntity tGowinClass, UriComponentsBuilder uriBuilder) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<TGowinClassEntity>> failures = validator.validate(tGowinClass);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		try{
			tGowinClassService.save(tGowinClass);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		//按照Restful风格约定，创建指向新任务的url, 也可以直接返回id或对象.
		String id = tGowinClass.getId();
		URI uri = uriBuilder.path("/rest/tGowinClassController/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody TGowinClassEntity tGowinClass) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<TGowinClassEntity>> failures = validator.validate(tGowinClass);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		try{
			tGowinClassService.saveOrUpdate(tGowinClass);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}

		//按Restful约定，返回204状态码, 无内容. 也可以返回200状态码.
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") String id) {
		tGowinClassService.deleteEntityById(TGowinClassEntity.class, id);
	}
}

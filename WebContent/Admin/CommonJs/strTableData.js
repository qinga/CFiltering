/**
 * 
 */
const strTableData = `
		<div class="loadBooksInfo">
		    <div class="layui-form">
			  <div class="layui-form-item" style="margin-top:10px;">
			    <label class="layui-form-label" style="padding: 9px 9px;">路线名称</label>
			    <div class="layui-input-block">
			      <input type="text" name="" required  lay-verify="required" autocomplete="off" class="layui-input" style="width:95%;">
			    </div>
			  </div>
			  <div class="layui-form-item">
			    <label class="layui-form-label" style="padding: 9px 9px;">路线标记</label>
			    <div class="layui-input-block">
			      <input type="text" name="" required lay-verify="required" autocomplete="off" class="layui-input" style="width:95%;">
			    </div>
			  </div>
			  <div class="layui-form-item">
			    <label class="layui-form-label" style="padding: 9px 9px;">路线选择</label>
			    <div class="layui-input-block" style="width:70%;">
			      <select name="bookFLag" lay-verify="required" lay-search>
			      	  <option></option>
			          <option>新手入门路线</option>
					  <option>其它学习路线</option>
			      </select>
			    </div>
			  </div>
			   <div class="layui-form-item layui-form-text">
			    <label class="layui-form-label" style="padding: 9px 9px;">方向概述</label>
			    <div class="layui-input-block">
			      <textarea name="desc" placeholder="请输入内容" class="layui-textarea" style="width:95%;"></textarea>
			    </div>
			  </div>
			  <div class="layui-form-item layui-form-text">
			    <label class="layui-form-label" style="padding: 9px 9px;">学习步骤</label>
			    <div class="layui-input-block">
			      <textarea name="desc" placeholder="请输入内容" class="layui-textarea" style="width:95%;"></textarea>
			    </div>
			  </div>
			  <div class="layui-upload">
				  <blockquote class="layui-elem-quote layui-quote-nm" style="margin-top: 10px;">
				    预览图：(双击可移除)
				    <div class="layui-upload-list" id="prePictureDiv"></div>
				 </blockquote>
			  </div>
			  <div class="layui-form-item">
			    <div class="layui-input-block">
			      <button class="layui-btn layui-btn-subm"  lay-filter="formDemo">提交编辑</button>
			      <button type="reset" class="layui-btn layui-btn-primary" style="background-color: #009688;color:white;">重置</button>
				 <button type="button" class="layui-btn testlay" id="editPicButton" style="float:right;">编辑图片</button> 
			    </div>
			  </div>
			</div>
		</div>`;
 export default strTableData;
import strTable from "../CommonJs/strTableData.js";
			$(function(){
				new Promise( (resolve, reject) => {
					layui.use('table', function(){
						  var table = layui.table;
						  table.render({
							id: "userLearnPath",
						    elem: '#uesrLearnPath',
						    //UserLearnPathAction_getLearnPath
						    url:'demo.json',
						    defaultToolbar: ['filter', 'exports'],
						    toolbar: '#toolbarDemo',
						    cols: [[
						      {field:'check',title:'',width:80,fixed:'left',type:"checkbox"},
						      {field:'id', title:'编号', width:80, fixed: 'left', unresize: true,hide:true},
						      {field:'name', title:'方向名称', width:120},
						      {field:'useCount', title:'使用人数'},
						      {field:'videoPath', title:'视频路径'},
						      {field:'markDirect', title:'路线简称'},
						      {field:'learnPathDetail', title:'路线步骤', event:"detail"},
						      {field:'processImgAddr', title:'路线图片', event:"pathImg"}
						    ]],
						    page: true 
						  });
						  resolve(table);
					   });
				}).then((table) => {
					/*var checkStatus = table.checkStatus('userLearnPath');
					console.log(checkStatus.data)*/
					/*table.on('row(uesrLearnPath)', function(obj){
					    var data = obj.data;
					    layer.alert(JSON.stringify(data), {
					      title: '当前行数据：'
					    });
					    
					    //标注选中样式
					    obj.tr.addClass('layui-table-click').siblings().removeClass('layui-table-click');
					  });*/
					table.on('toolbar(uesrLearnPath)', function(obj){
						var _obj = obj;
						  var checkStatus = table.checkStatus(obj.config.id);
						  switch(obj.event){
						    case 'add':
						    	layui.use(['layer','form','upload'], function(){
					   				 var layer = layui.layer;
					   				 var form = layui.form;
					   				 var upload = layui.upload;
					   				 layer.open({
					   					 type: 1,
					   					 area: '420px',
					   					 shadeClose: 'true',
					   					 maxHeight: 700,
					   					 scrollbar: false,
					   					 title: "添加方向路线",
					   					 content: strTable
					   				 });
					   				 form.render();
									 //执行文件上传实例
								     upload.render({
										    elem: '.testlay', //绑定元素
										    url: '/upload/', //上传接口
										    auto: false,
										    bindAction: ".layui-btn-subm",
										    accept:"file",
										    multiple: true,
										    choose: function(obj){
										        //将每次选择的文件追加到文件队列
										        var file = obj.pushFile();
										        //预读本地文件示例，不支持ie8
										     obj.preview(function(index, file, result){
										    	$(".layui-layer-content").find('#prePictureDiv').append("<img src='"+result+"' alt=\"file.name\" class=\"layui-upload-img\" style=\"width:70px;height:auto;\"/>");
				        					    console.log(result)
										    	var $uploadImg = $(".layui-upload-img");
					   					        $.each($uploadImg, function(k,v) {
					   					        	$(v).on("click", function() {
					   					        		$(this).remove();
					   					        	})
					   					        })
										      });
										    },
										    done: function(res){
										      //上传完毕回调
										    },
										    error: function(){
										      //请求异常回调
										    }
										 });
					   		   		});
						    break;
						    case 'delete':
						    	
						        layer.confirm('真的删除行么', function(index){
						        	
						            _obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
						            layer.close(index);
						            //向服务端发送删除指令
						          });
						    break;
						    case 'update':
						      layer.msg('编辑');
						    break;
						  };
						});
					table.on('checkbox(uesrLearnPath)', function(obj){
		        		  console.log(obj.checked); //当前是否选中状态
		        		  console.log(obj.data); //选中行的相关数据
		        		  console.log(obj.type); //如果触发的是全选，则为：all，如果触发的是单选，则为：one
		        	});
					table.on('tool(uesrLearnPath)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
						  var data = obj.data; //获得当前行数据
						  var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
						  var tr = obj.tr; //获得当前行 tr 的DOM对象
						 console.log("tool");
						  if(layEvent === 'detail'){ //查看
						    //do somehing
							  console.log(obj.data)
						  } else if(layEvent === 'del'){ //删除
						    layer.confirm('真的删除行么', function(index){
						      obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
						      layer.close(index);
						      //向服务端发送删除指令
						    });
						  } else if(layEvent === 'edit'){ //编辑
						    //do something
						    
						    //同步更新缓存对应的值
						    obj.update({
						      username: '123'
						      ,title: 'xxx'
						    });
						  }
						});
				}, () => {
					
				}).catch( (err) => {
					console.log(err)
				});
			})
			
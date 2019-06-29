
/*! 
 * jQuery Validation Plugin v1.14.0 
 * 
 * http://jqueryvalidation.org/ 
 */  
(function( factory ) {  
    if ( typeof define === "function" && define.amd ) {  
        define( ["jquery"], factory );  
    } else {  
        factory( jQuery );  
    }  
}(function( $ ) {  
  
$.extend($.fn, {  
    // http://jqueryvalidation.org/validate/  
    validate: function( options ) {  
  
        if ( !this.length ) {  
            if ( options && options.debug && window.console ) {  
                console.warn( "Nothing selected, can't validate, returning nothing." );  
            }  
            return;  
        }  
  
        // 避免生成$.validator的多个实例  
        var validator = $.data( this[ 0 ], "validator" );  
        if ( validator ) {  
            return validator;  
        }  
  
        // Add novalidate tag if HTML5.  
        this.attr( "novalidate", "novalidate" );  
  
        // 生成$.validator实例，并将用户配置写入该实例中  
        // 生成的实例记录在dom元素的data属性中，实现 <单例模式>  
        // 记录在data属性中既可以避免生成多个validator实例，同时jquery方法也可以通过读取data属性获得该实例  
        validator = new $.validator( options, this[ 0 ] );  
        $.data( this[ 0 ], "validator", validator );  
  
        if ( validator.settings.onsubmit ) {  
  
            // 带有type="submit"属性的按钮绑定点击事件  
            this.on( "click.validate", ":submit", function( event ) {  
                if ( validator.settings.submitHandler ) {  
                    validator.submitButton = event.target;  
                }  
  
                // 带有type="submit"属性的按钮有cancel样式类时，阻止验证，只提交???  
                if ( $( this ).hasClass( "cancel" ) ) {  
                    validator.cancelSubmit = true;  
                }  
  
                // 带有type="submit"属性的按钮有formnovalidate属性且为真时，阻止验证，只提交???  
                if ( $( this ).attr( "formnovalidate" ) !== undefined ) {  
                    validator.cancelSubmit = true;  
                }  
            });  
  
            // validate the form on submit  
            this.on( "submit.validate", function( event ) {  
                if ( validator.settings.debug ) {  
                    // validator.settings.debug设置为真时，阻止表单提交，只验证  
                    event.preventDefault();  
                }  
  
                function handle() {  
                    var hidden, result;  
                    // 当用户配置了配置了submitHandler函数，根据其返回值阻止或实现表单提交，无返回值默认阻止  
                    if ( validator.settings.submitHandler ) {  
                        if ( validator.submitButton ) {  
                            // 将type="submit"按钮中的value值放在特定的隐藏域中  
                            hidden = $( "<input type='hidden'/>" )  
                                .attr( "name", validator.submitButton.name )  
                                .val( $( validator.submitButton ).val() )  
                                .appendTo( validator.currentForm );  
                        }  
                        result = validator.settings.submitHandler.call( validator, validator.currentForm, event );  
                        if ( validator.submitButton ) {  
                            // 放置type="submit"元素数据的隐藏域清空  
                            hidden.remove();  
                        }  
                        if ( result !== undefined ) {  
                            return result;  
                        }  
                        return false;  
                    }  
                    return true;  
                }  
  
                // prevent submit for invalid forms or custom submit handlers  
                if ( validator.cancelSubmit ) {  
                    validator.cancelSubmit = false;  
                    return handle();  
                }  
  
                // 调用validator.form()验证表单，显示错误文案或执行success回调，返回验证结果为真时执行handle作提交处理，  
                // 为否时执行validator.focusInvalid()函数，首个待验证元素获得焦点  
                if ( validator.form() ) {  
                    if ( validator.pendingRequest ) {  
                        validator.formSubmitted = true;  
                        return false;  
                    }  
                    return handle();  
                } else {  
                    validator.focusInvalid();  
                    return false;  
                }  
            });  
        }  
  
        return validator;// 链式调用  
    },  
  
    // 元素是表单时，执行表单验证，元素是表单元素时，执行单个元素验证  
    valid: function() {  
        var valid, validator, errorList;  
  
        if ( $( this[ 0 ] ).is( "form" ) ) {  
            valid = this.validate().form();  
        } else {  
            errorList = [];  
            valid = true;  
            validator = $( this[ 0 ].form ).validate();  
            this.each( function() {  
                valid = validator.element( this ) && valid;  
                errorList = errorList.concat( validator.errorList );  
            });  
            validator.errorList = errorList;  
        }  
        return valid;  
    },  
  
    // 当command参数非空时,添加或移除验证规则  
    //     $(element).rules("add",{  
    //           required: true,  
    //           minlength: 2,  
    //           messages: {  
    //               required: "Required input",  
    //               minlength: jQuery.validator.format("Please, at least {0} characters are necessary")  
    //           }  
    //     });  
    //     rules("remove",rules);返回清除的验证规则  
    // 其余情况，获取样式、属性、data属性、js中设置的元素的验证规则，并转换成简单的键值对形式，保证先验证值存在与否，最后远程验证      
    rules: function( command, argument ) {  
        var element = this[ 0 ],  
            settings, staticRules, existingRules, data, param, filtered;  
  
        if ( command ) {  
            // 通过data方法获得validator实例  
            settings = $.data( element.form, "validator" ).settings;  
            staticRules = settings.rules;  
            // 拷贝对象，$.validator.staticRules从$.validator.setting.rules获取元素的验证规则  
            existingRules = $.validator.staticRules( element );  
            switch ( command ) {  
            case "add":  
                // $.validator.normalizeRule将argument验证规则转换成对象形式  
                $.extend( existingRules, $.validator.normalizeRule( argument ) );  
  
                delete existingRules.messages;// 清空$.validator.setting.rules[element.name]中的messages属性  
                staticRules[ element.name ] = existingRules;// 通过引用对象，更新$.validator.setting.rules配置项  
                if ( argument.messages ) {  
                    // 通过引用对象，更新$.validator.setting.messages配置项  
                    settings.messages[ element.name ] = $.extend( settings.messages[ element.name ], argument.messages );  
                }  
                break;  
            case "remove":  
                if ( !argument ) {  
                    delete staticRules[ element.name ];  
                    return existingRules;  
                }  
                filtered = {};  
                $.each( argument.split( /\s/ ), function( index, method ) {  
                    filtered[ method ] = existingRules[ method ];  
                    delete existingRules[ method ];//argument为空时清空js中配置的验证规则，现在为何不清除相应的规则？？？  
                    if ( method === "required" ) {  
                        $( element ).removeAttr( "aria-required" );  
                    }  
                });  
                return filtered;  
            }  
        }  
  
        data = $.validator.normalizeRules( // 将设置了验证条件depends、函数function(element)、最小最大值的验证规则转换成key-value键值对形式;  
        $.extend(  
            {},  
            $.validator.classRules( element ),// 获取样式中设置的验证规则  
            $.validator.attributeRules( element ),// 获取属性中设置的验证规则  
            $.validator.dataRules( element ), // 获取data属性中设置的验证规则  
            $.validator.staticRules( element ) // 获取js里设置验证规则  
        ), element );  
  
        // 首先验证值是否存在  
        if ( data.required ) {  
            param = data.required;  
            delete data.required;  
            data = $.extend( { required: param }, data );  
            $( element ).attr( "aria-required", "true" );  
        }  
  
        // 最后执行远程验证  
        if ( data.remote ) {  
            param = data.remote;  
            delete data.remote;  
            data = $.extend( data, { remote: param });  
        }  
  
        return data;  
    }  
});  
  
// 向jquery的伪类选择器中添加方法※※※  
$.extend( $.expr[ ":" ], {  
    // 返回为空值的元素  
    blank: function( a ) {  
        return !$.trim( "" + $( a ).val() );  
    },  
    // 返回有值的元素  
    filled: function( a ) {  
        return !!$.trim( "" + $( a ).val() );  
    },  
    // 返回选中的元素  
    unchecked: function( a ) {  
        return !$( a ).prop( "checked" );  
    }  
});  
  
// constructor for validator  
// 传入参数是用户自定义的配置项，以及当前调用validate方法的表单;  
// 配置项与默认配置混合写入this.setting属性中，当前验证的form表单写入this.currentForm中;  
// $.validator作为构造函数，this.attribute取其中的属性  
$.validator = function( options, form ) {  
    // 默认配置以及用户配置写入this.setting函数中  
    this.settings = $.extend( true, {}, $.validator.defaults, options );  
    this.currentForm = form;  
    this.init();  
};  
  
// 格式化错误文本，业务场景是填充最大最小值的错误提示文本  
/** 
 * [format description] 
 * @param  {string} source 待替换的错误提示文本，通常是输入框的最大最小长度等 
 * @param  {array|string|null} params 当传入数组或字符串时，遍历依次替换source中内容 
 *                                    当没有传值时，$.validator.format返回待替换文本的模板函数，再传入替换文本实现替换 
 * @return {[type]}         $.validator.format只有一个实参时，返回待替换文本的模板函数 
 *                          有多个实参时，直接将待替换文本替换为理想内容 
 * 示例： 
 *      var template = jQuery.validator.format("{0} is not a valid value"); 
 *      later, results in 'abc is not a valid value' 
 *      alert(template("abc")); 
 */  
$.validator.format = function( source, params ) {  
    if ( arguments.length === 1 ) {  
        return function() {  
            var args = $.makeArray( arguments );  
            args.unshift( source );  
            return $.validator.format.apply( this, args );  
        };  
    }  
    if ( arguments.length > 2 && params.constructor !== Array  ) {  
        params = $.makeArray( arguments ).slice( 1 );  
    }  
    if ( params.constructor !== Array ) {  
        params = [ params ];  
    }  
    $.each( params, function( i, n ) {  
        source = source.replace( new RegExp( "\\{" + i + "\\}", "g" ), function() {  
            return n;  
        });  
    });  
    return source;  
};  
  
/** 
 * defaults默认配置项，和用户自定义的配置项通过setDefaluts方法写入this.setting对象中; 
 * setDefaults由默认配置项和用户自定义的配置项获取this.setting; 
 * messages默认的错误提示文本; 
 * autoCreateRanges为真时，将min、max配置项自动转化成range配置; 
 * prototype原型; 
 * classRuleSettings默认可以添加到Dom元素类中的验证规整，包括required、digits等; 
 * addClassRules添加可以向Dom元素添加的样式规则类; 
 * classRules由Dom元素的样式规则类获取真实的验证规则; 
 * normalizeAttributeRule(rules,type,method,value)向rules对象添加验证规则，通常是{method:value}; 
 * attributeRules由Dom元素的验证规则同名方法属性获取验证规则，type属性简化验证规则设置成true形式，调用normalizeAttributeRule实现; 
 * dataRules由Dom元素的形如data-ruleRequired属性获取验证规则，调用normalizeAttributeRule实现; 
 * staticRules调用$.validator.normalizeRule函数从validator.settings.rules获取用户自定义的元素验证规则; 
 * normalizeRules将设置了验证条件depends（该条件成立的前提下）、函数function(element)、最小最大值的验证规则转换成key-value键值对形式; 
 * normalizeRule将字符串形式的验证规则转换成对象形式，element:"required"转换成{"required":true}; 
 * addMethod添加验证规则，更新methods、messages配置，向元素添加样式规则类执行验证; 
 * methods具体的验证逻辑;  
 */  
$.extend( $.validator, {  
  
    // 默认配置项  
    /** 
     * 错误提示元素加上errorClass类，id属性为name+"-error"，以及添加for=name属性; 
     * 验证元素或者表单添加aria-describedby，关联相应的错误提示（通过错误提示的id属性）; 
     * 
     *  
     * messages错误提示文本; 
     * groups编成组使用同一套验证规则的Dom元素，形如groups:{keyname:"elementName elementName"}, 
     *      执行时替换为{{elementName:keyname},{elementName:keyname}}, 
     *      通过keyname执行同一套验证规则，获取待验证元素时又根据元素的name属性，而不是keyname; 
     * rules验证规则，{elementName:{required:true}}形式; 
     * focusCleanup为真时，元素获得焦点时移除错误提示; 
     * focusInvalid为真时，未通过验证的首个表单元素获得焦点; 
     * validClass验证前待验证元素的样式？？？; 
     * errorClass错误提示加载的样式类，默认为error，支持用户自定义，可以传入多个样式，"errorClass errorClass"; 
     * errorElement用哪种html标签包裹错误提示，默认为label，支持用户自定义; 
     * errorContainer通常配合errorLabelContainer使用，有错误提示时容器显示，无错误提示容器隐藏，展示错误提示时创建; 
     * errorLabelContainer把错误提示统一放在一个容器里，页面中已存在的元素; 
     * wrapper用哪种标签再将errorElement包裹起来; 
     *      提示：errorContainer、errorLabelContainer、wrapper通常一同使用，用来将错误提示集体放在一个容器中，自动显示隐藏 
     *          errorContainer: "div.error", 
     *          errorLabelContainer: $("#signupForm div.error"), 
     *          wrapper: "li" 
     *    aria-describedby属性在错误提示元素没有label包裹以及有groups组合的情况下添加，使验证元素对应错误提示; 
     * onSubmit为真时，提交时验证，为否时改用其他情形验证; 
     * onfocusout失去焦点时验证; 
     * onkeyup键盘按钮松开时验证; 
     * onclick点击复选框及单选框时验证; 
     * ignore略过验证的元素; 
     * ignoreTitle为否提示文案可以设置验证元素的title属性中; 
     * 
     *  
     * onfucsin得到焦点时清楚错误提示，调用unhighlight方法清楚错误提示的相应样式并调用hideThese隐藏错误提示; 
     * onfoucsout失去焦点时先调用this.optional执行非空验证（其他验证的依赖条件）， 
     *      再根据this.submitted验证元素的其他验证条件匹配与否; 
     * onkeyup先排除一些特殊按键，再检验未通过验证的元素或者最后一个执行验证的元素即被修改的元素; 
     * onclick验证未通过验证的复选框、下拉框、单选框; 
     * highlight添加错误样式; 
     * unhighlight移除错误样式; 
     * 
     *  
     * errorPlacement(error,$element)放置错误提示文本，error错误提示元素，$element验证的元素; 
     * success每次验证通过执行的回调或者给错误提示文本元素添加的样式; 
     * showErrors自定义配置，替代默认的showDefaultErrors，失败时展示错误文本，成功时执行success回调; 
     * invalidHandler表单未通过验证时触发的回调函数，默认无; 
     */  
    defaults: {  
        messages: {},  
        groups: {},  
        rules: {},  
        errorClass: "error",  
        validClass: "valid",  
        errorElement: "label",  
        focusCleanup: false,  
        focusInvalid: true,  
        errorContainer: $( [] ),  
        errorLabelContainer: $( [] ),  
        onsubmit: true,  
        ignore: ":hidden",  
        ignoreTitle: false,  
        onfocusin: function( element ) {  
            this.lastActive = element;  
  
            if ( this.settings.focusCleanup ) {  
                if ( this.settings.unhighlight ) {  
                    this.settings.unhighlight.call( this, element, this.settings.errorClass, this.settings.validClass );  
                }  
                this.hideThese( this.errorsFor( element ) );  
            }  
        },  
        onfocusout: function( element ) {  
            if ( !this.checkable( element ) && ( element.name in this.submitted || !this.optional( element ) ) ) {  
                this.element( element );  
            }  
        },  
        onkeyup: function( element, event ) {  
            // Avoid revalidate the field when pressing one of the following keys  
            // Shift       => 16  
            // Ctrl        => 17  
            // Alt         => 18  
            // Caps lock   => 20  
            // End         => 35  
            // Home        => 36  
            // Left arrow  => 37  
            // Up arrow    => 38  
            // Right arrow => 39  
            // Down arrow  => 40  
            // Insert      => 45  
            // Num lock    => 144  
            // AltGr key   => 225  
            var excludedKeys = [  
                16, 17, 18, 20, 35, 36, 37,  
                38, 39, 40, 45, 144, 225  
            ];  
  
            // tab键 event.which===9  
            if ( event.which === 9 && this.elementValue( element ) === "" || $.inArray( event.keyCode, excludedKeys ) !== -1 ) {  
                return;  
            } else if ( element.name in this.submitted || element === this.lastElement ) {  
                this.element( element );  
            }  
        },  
        onclick: function( element ) {  
            // click on selects, radiobuttons and checkboxes  
            if ( element.name in this.submitted ) {  
                this.element( element );  
  
            // or option elements, check parent select in that case  
            } else if ( element.parentNode.name in this.submitted ) {  
                this.element( element.parentNode );  
            }  
        },  
        highlight: function( element, errorClass, validClass ) {  
            if ( element.type === "radio" ) {  
                this.findByName( element.name ).addClass( errorClass ).removeClass( validClass );  
            } else {  
                $( element ).addClass( errorClass ).removeClass( validClass );  
            }  
        },  
        unhighlight: function( element, errorClass, validClass ) {  
            if ( element.type === "radio" ) {  
                this.findByName( element.name ).removeClass( errorClass ).addClass( validClass );  
            } else {  
                $( element ).removeClass( errorClass ).addClass( validClass );  
            }  
        }  
    },  
  
    setDefaults: function( settings ) {  
        $.extend( $.validator.defaults, settings );  
    },  
  
    // 默认的错误提示文本  
    messages: {  
        required: "This field is required.",  
        remote: "Please fix this field.",  
        email: "Please enter a valid email address.",  
        url: "Please enter a valid URL.",  
        date: "Please enter a valid date.",  
        dateISO: "Please enter a valid date ( ISO ).",  
        number: "Please enter a valid number.",  
        digits: "Please enter only digits.",  
        creditcard: "Please enter a valid credit card number.",  
        equalTo: "Please enter the same value again.",  
        maxlength: $.validator.format( "Please enter no more than {0} characters." ),  
        minlength: $.validator.format( "Please enter at least {0} characters." ),  
        rangelength: $.validator.format( "Please enter a value between {0} and {1} characters long." ),  
        range: $.validator.format( "Please enter a value between {0} and {1}." ),  
        max: $.validator.format( "Please enter a value less than or equal to {0}." ),  
        min: $.validator.format( "Please enter a value greater than or equal to {0}." )  
    },  
  
    // min、max配置项是否自动转化成range配置  
    autoCreateRanges: false,  
  
    /** 
     * this.labelContainer错误提示的包裹元素，页面中已存在的元素; 
     * this.errorContent错误提示的包裹元素或者待验证的表单; 
     * this.containers错误提示统一放置在某个元素中，在html事先写就，一份验证表单只有一个; 
     *  
     * 
     * this.currentForm记录当前待验证的表单; 
     * this.currentElements记录待验证的Dom元素; 
     * this.lastElement记录最后一个被验证的元素; 
     * this.lastActive记录获得焦点元素的name值; 
     *  
     * 
     * this.successList以[element,element]形式更新验证成功元素列表; 
     * this.errorList以{message:message,element:element,method:rule.method}更新验证失败元素列表; 
     * this.errorMap以{element.name:message}更新元素的错误提示列表，以供错误文本展示用; 
     * this.submitted以{element.name:message}更新列表，通常与errorMap等值，在绑定事件如onfocusout、onkeyup中使用; 
     * this.inValid未通过验证的表单元素，已验证与errorMap等值，未验证为空，被element函数调用，为空时显示this.container; 
     * this.toShow以[errorElement,errorElement]更新展示的错误提示文本元素; 
     * this.toHide通过prepareForm方法初始化设置待验证元素对应的错误提示文本元素，执行验证后滤过this.toShow中的元素; 
     * 
     * 
     * this.pendingRequest用来记录所有远程验证通过后再执行表单; 
     *  
     *  
     * init通过groups打包需要应用同一验证规则的Dom元素，rules获取验证规则, 
     *      绑定onfoucsin/onfoucsut/onkeyup/onclick事件，通过delegate函数函数获取setting中设置的事件处理函数, 
     *      绑定表单未通过验证时触发的回调函数this.settings.invalidHandler, 
     *      向需要进行required验证的Dom元素添加aria-required="true"属性; 
     * form调用formCheck执行验证，更新successList/errosList/errorMap/toShow/toHide/submitted列表, 
     *      触发invalid-form事件，显示错误文本或执行success回调，调用this.valid()返回验证结果true/false; 
     * checkForm调用check方法执行验证，并更新successList/errosList/errorMap/submitted列表，调用this.valid()返回验证结果; 
     * element调用prepareElement清空this.successList/errosList/errorMap/toShow/toHide/currentElement. 
     *      调用check执行验证，并更新this.successList/errosList/errorMap/subbmitted. 
     *      更新invalid列表（首次验证时为未通过验证的元素，未验证为空），显示错误文本； 
     * showErrors远程验证时传入errors更新successList及errorList，调用用户自定义的showErrors方法 
     *      或执行defaultShowErrors方法展示错误提示文本、执行success回调; 
     * resetForm重置表单，清除并隐藏错误文本，清除各类缓存列表this.successList/errosList/errorMap/toShow/toHide/currentElement; 
     * numberOfInvalids获取this.invalid列表的长度; 
     * objectLength返回对象字面量有多少个键; 
     * hideErrors调用hideThese方法清空错误提示并隐藏相应元素; 
     * hideThese清楚错误提示文本，隐藏错误提示元素及其包裹元素; 
     * valid调用size方法判断错误元素列表this.errorList是否有值; 
     * size判断错误元素列表this.errorList是否有值; 
     * focusInvalid获得焦点的元素或者表单中首个待验证的元素触发focusin事件，利用try-catch阻止报错; 
     * findLastActive获得焦点元素的name值，利用$.grep()比对this.errorList是否有此值,this.lastActive记录获得焦点元素的name值; 
     * elements返回待验证的Dom元素; 
     * clean由参数获取首个元素;  
     * errors获取错误提示元素或者其包裹元素; 
     * reset重置缓存信息，包括this.successList/errosList/errorMap/toShow/toHide/currentElement; 
     * prepareForm调用this.reset()清空successList/errosList/errorMap/toShow/toHide/currentElement,错误提示均记录为toHide; 
     * prepareElement调用this.reset()清空successList/errosList/errorMap/toShow/toHide/currentElement,指定元素的错误提示记录为toHide; 
     * elementValue获取表单元素的值; 
     * check验证Dom元素，验证失败时调用formatAndAdd方法更新错误元素列表errorList，错误文本列表errorMap，以及this.submitted 
     *      验证成功时更新成功元素列表successList; 
     * 
     * customDataMessage返回data属性中设置的错误提示文本，形如data-msgRequired=""; 
     * customMessage返回js中设置的错误提示文本; 
     * findDefined从js配置文件、data属性、title属性中找到有值的第一条错误提示文本; 
     * defaultMessage调用findDefined方法返回错误文本; 
     * formatAndAdd填充并获得错误文本，errorList、errorMap、submitted中添加值; 
     * addWrapper向jquery对象添加错误提示包裹元素this.setting.wrapper; 
     * defaultShowErrors调用showLabel方法添加错误提示文本元素或者执行success回调，更新隐藏的错误提示文本元素列表toHide，展示的错误提示文本元素列表toShow; 
     * invalidElements返回通过验证的元素列表; 
     * validElements返回未通过验证的元素列表; 
     * showLabel验证失败时更新错误提示文本内容，添加样式并显示，成功时执行success回调或添加样式，更新toShow列表; 
     * errorsFor在执行errors函数的基础上获取label[for='+name+']或者带有aria-describedby属性的错误提示元素; 
     *      aria-describedby属性在错误提示元素没有label包裹以及有groups组合的情况下添加，使验证元素对应错误提示; 
     * idOrName对一组表单元素（比如多个checkbox/radio、以及用户自定义的一组验证）验证取name属性，否则取id; 
     * validationTargetFor待验证元素以jquery对象的形式返回，排除复选框和单选框; 
     * checkable判断表单元素是否checkbox/radio类型; 
     * findByName找到name属性为参数值的DOM节点; 
     * getLength判断选中的复选框、单选框、下拉框是否有值; 
     * depend判断元素执行验证前的依赖条件是否成立; 
     * dependTypes返回依赖条件的最终值，业务逻辑是判断某元素是否可见，以及元素是否符合特定条件; 
     * optional元素没有值或者有值的复选框、单选框未被选中的情况下，返回"dependency-mismatch", 
     *      元素有值的情况下返回false; 
     * startRequest使this.pendingRequest自增1，发送远程验证的个数; 
     * stopRequest远程验证全通过后，提交表单或触发invalid-form事件; 
     * previousValue缓存上一次的验证结果valid、值old、提示文本message（可能是后台返回的错误文本）、用户配置错误提示originalMessage， 
     *      当前验证的值与此前相同，返回前一次的验证结果，不同，更新data缓存信息; 
     * destroy重置表单，解绑.validate事件，移除属性中的实例; 
     */  
    prototype: {  
  
        init: function() {  
            this.labelContainer = $( this.settings.errorLabelContainer );  
            this.errorContext = this.labelContainer.length && this.labelContainer || $( this.currentForm );  
            this.containers = $( this.settings.errorContainer ).add( this.settings.errorLabelContainer );  
            this.submitted = {};  
            this.valueCache = {};  
            this.pendingRequest = 0;  
            this.pending = {};  
            this.invalid = {};  
            this.reset();  
  
            var groups = ( this.groups = {} ),  
                rules;  
            $.each( this.settings.groups, function( key, value ) {  
                if ( typeof value === "string" ) {  
                    value = value.split( /\s/ );  
                }  
                $.each( value, function( index, name ) {  
                    groups[ name ] = key;  
                });  
            });  
  
            rules = this.settings.rules;  
            $.each( rules, function( key, value ) {  
                rules[ key ] = $.validator.normalizeRule( value );  
            });  
  
            // 代理事件，初始化设置default及用户自定义的onfocusin、onfocusout、onclick、onclick函数  
            function delegate( event ) {  
                var validator = $.data( this.form, "validator" ),  
                    eventType = "on" + event.type.replace( /^validate/, "" ),  
                    settings = validator.settings;  
                if ( settings[ eventType ] && !$( this ).is( settings.ignore ) ) {  
                    settings[ eventType ].call( validator, this, event );  
                }  
            }  
  
            // 绑定事件  
            // 添加type='password/number/search/tel/url/email/datetime/date/month/week/time/datetime-local/range/color'等  
            // 验证时仍需要添加规则样式类、规则属性、规则data属性或js中设置的规则  
            $( this.currentForm )  
                .on( "focusin.validate focusout.validate keyup.validate",  
                    ":text, [type='password'], [type='file'], select, textarea, [type='number'], [type='search'], " +  
                    "[type='tel'], [type='url'], [type='email'], [type='datetime'], [type='date'], [type='month'], " +  
                    "[type='week'], [type='time'], [type='datetime-local'], [type='range'], [type='color'], " +  
                    "[type='radio'], [type='checkbox']", delegate)  
                // Support: Chrome, oldIE  
                // "select" is provided as event.target when clicking a option  
                .on("click.validate", "select, option, [type='radio'], [type='checkbox']", delegate);  
  
            // 绑定表单未通过验证时触发的回调函数  
            if ( this.settings.invalidHandler ) {  
                $( this.currentForm ).on( "invalid-form.validate", this.settings.invalidHandler );  
            }  
  
            // Add aria-required to any Static/Data/Class required fields before first validation  
            // Screen readers require this attribute to be present before the initial submission http://www.w3.org/TR/WCAG-TECHS/ARIA2.html  
            $( this.currentForm ).find( "[required], [data-rule-required], .required" ).attr( "aria-required", "true" );  
        },  
  
        // 调用checkform方法执行验证，并更新错误元素列表errorList、错误文本列表errorMap、成功元素列表successList，以及this.submitted  
        // 触发invalid-form事件，显示错误文本或执行success回调，返回验证结果true/false  
        form: function() {  
            this.checkForm();  
            $.extend( this.submitted, this.errorMap );  
            this.invalid = $.extend({}, this.errorMap );  
            if ( !this.valid() ) {  
                $( this.currentForm ).triggerHandler( "invalid-form", [ this ]);  
            }  
            this.showErrors();  
            return this.valid();  
        },  
  
        // 调用check方法执行验证，并更新错误元素列表errorList、错误文本列表errorMap、成功元素列表successList，以及this.submitted，返回验证结果  
        checkForm: function() {  
            this.prepareForm();  
            for ( var i = 0, elements = ( this.currentElements = this.elements() ); elements[ i ]; i++ ) {  
                this.check( elements[ i ] );  
            }  
            return this.valid();  
        },  
  
        // 对单个元素进行验证，更新this.invalid列表（首次验证后this.invalid赋值为未通过验证元素的集合，其他情况为空对象）  
        // 调用prepareElement方法清空successList/errosList/errorMap/toShow/toHide/currentElement  
        // 调用check执行验证，并更新successList/errosList/errorMap/submitted列表，showErrors显示错误文本  
        element: function( element ) {  
            var cleanElement = this.clean( element ),// 选取收个元素  
                checkElement = this.validationTargetFor( cleanElement ),// 包装成jquery对象  
                result = true;  
  
            this.lastElement = checkElement;  
  
            if ( checkElement === undefined ) {  
                delete this.invalid[ cleanElement.name ];  
            } else {  
                this.prepareElement( checkElement );  
                this.currentElements = $( checkElement );  
  
                result = this.check( checkElement ) !== false;  
                if ( result ) {  
                    delete this.invalid[ checkElement.name ];  
                } else {  
                    this.invalid[ checkElement.name ] = true;  
                }  
            }  
  
            $( element ).attr( "aria-invalid", !result );  
  
            if ( !this.numberOfInvalids() ) {// 获取this.invalid列表的长度  
                this.toHide = this.toHide.add( this.containers );  
            }  
            this.showErrors();  
            return result;  
        },  
  
        // 当传入参数errors(远程验证时触发)，清空并更新错误元素列表errorList，更新成功元素列表successList  
        // 执行用户自定义的showErrors方法，或者执行默认的defaultShowErrors方法  
        showErrors: function( errors ) {  
            if ( errors ) {  
                // add items to error list and map  
                $.extend( this.errorMap, errors );  
                this.errorList = [];  
                for ( var name in errors ) {  
                    this.errorList.push({  
                        message: errors[ name ],  
                        element: this.findByName( name )[ 0 ]  
                    });  
                }  
                // remove items from success list  
                this.successList = $.grep( this.successList, function( element ) {  
                    return !( element.name in errors );  
                });  
            }  
            if ( this.settings.showErrors ) {  
                this.settings.showErrors.call( this, this.errorMap, this.errorList );  
            } else {  
                this.defaultShowErrors();  
            }  
        },  
  
        // 重置表单，清除并隐藏错误文本，清除各类缓存列表this.successList/errosList/errorMap/toShow/toHide/currentElement  
        resetForm: function() {  
            if ( $.fn.resetForm ) {  
                $( this.currentForm ).resetForm();  
            }  
            this.submitted = {};  
            this.lastElement = null;  
            this.prepareForm();  
            this.hideErrors();  
            var i, elements = this.elements()  
                .removeData( "previousValue" )  
                .removeAttr( "aria-invalid" );  
  
            if ( this.settings.unhighlight ) {  
                for ( i = 0; elements[ i ]; i++ ) {  
                    this.settings.unhighlight.call( this, elements[ i ],  
                        this.settings.errorClass, "" );  
                }  
            } else {  
                elements.removeClass( this.settings.errorClass );  
            }  
        },  
  
        // 获取this.invalid列表的长度  
        numberOfInvalids: function() {  
            return this.objectLength( this.invalid );  
        },  
  
        // 返回对象字面量有多少键  
        objectLength: function( obj ) {  
            /* jshint unused: false */  
            var count = 0,  
                i;  
            for ( i in obj ) {  
                count++;  
            }  
            return count;  
        },  
  
        // 调用hideThese清空错误提示文本，并隐藏错误提示文本及其包裹元素  
        hideErrors: function() {  
            this.hideThese( this.toHide );  
        },  
  
        // 清空错误提示文本，并隐藏错误提示文本及其包裹元素  
        hideThese: function( errors ) {  
            errors.not( this.containers ).text( "" );  
            this.addWrapper( errors ).hide();  
        },  
  
        // 调用size方法判断错误元素列表this.errorList是否有值  
        valid: function() {  
            return this.size() === 0;  
        },  
  
        // 判断错误元素列表this.errorList是否有值  
        size: function() {  
            return this.errorList.length;  
        },  
  
        // 获得焦点的元素或者表单中首个待验证的元素触发focusin事件，利用try-catch阻止报错;  
        focusInvalid: function() {  
            if ( this.settings.focusInvalid ) {  
                try {  
                    $( this.findLastActive() || this.errorList.length && this.errorList[ 0 ].element || [])  
                    .filter( ":visible" )  
                    .focus()  
                    // manually trigger focusin event; without it, focusin handler isn't called, findLastActive won't have anything to find  
                    .trigger( "focusin" );  
                } catch ( e ) {  
                    // ignore IE throwing errors when focusing hidden elements  
                }  
            }  
        },  
  
        // 获得焦点元素的name值，利用$.grep()比对this.errorList是否有此值,this.lastActive记录获得焦点元素的name值  
        findLastActive: function() {  
            var lastActive = this.lastActive;// 获得焦点元素的name值  
            return lastActive && $.grep( this.errorList, function( n ) {  
                return n.element.name === lastActive.name;  
            }).length === 1 && lastActive;  
        },  
  
        // 返回待验证的元素  
        elements: function() {  
            var validator = this,  
                rulesCache = {};// 闭包用来缓存name属性，以使name属性相同的首个Dom元素被选中  
  
            // select all valid inputs inside the form (no submit or reset buttons)  
            return $( this.currentForm )  
            .find( "input, select, textarea" )  
            .not( ":submit, :reset, :image, :disabled" )  
            .not( this.settings.ignore )  
            .filter( function() {  
                if ( !this.name && validator.settings.debug && window.console ) {  
                    console.error( "%o has no name assigned", this );  
                }  
  
                // select only the first element for each name, and only those with rules specified  
                if ( this.name in rulesCache || !validator.objectLength( $( this ).rules() ) ) {  
                    return false;  
                }  
  
                rulesCache[ this.name ] = true;  
                return true;  
            });  
        },  
  
        // 获取首个由选择器获得的Dom元素  
        clean: function( selector ) {  
            return $( selector )[ 0 ];  
        },  
  
        // 获取错误提示元素或其包裹元素  
        errors: function() {  
            var errorClass = this.settings.errorClass.split( " " ).join( "." );  
            return $( this.settings.errorElement + "." + errorClass, this.errorContext );  
        },  
  
        // 清空successList/errosList/errorMap/toShow/toHide/currentElement  
        reset: function() {  
            this.successList = [];  
            this.errorList = [];  
            this.errorMap = {};  
            this.toShow = $( [] );  
            this.toHide = $( [] );  
            this.currentElements = $( [] );  
        },  
  
        // 调用this.reset()清空successList/errosList/errorMap/toShow/toHide/currentElement,错误提示均记录为toHide;  
        prepareForm: function() {  
            this.reset();  
            this.toHide = this.errors().add( this.containers );  
        },  
  
        // 调用reset清空缓存数据，制定元素存入toHide列表中  
        prepareElement: function( element ) {  
            this.reset();  
            this.toHide = this.errorsFor( element );  
        },  
  
        // 获取Dom元素的值，当Dom元素有html5的validity属性（且该属性下的badInput为真）时，不获取表单元素的值  
        elementValue: function( element ) {  
            var val,  
                $element = $( element ),  
                type = element.type;  
  
            if ( type === "radio" || type === "checkbox" ) {  
                return this.findByName( element.name ).filter(":checked").val();  
            } else if ( type === "number" && typeof element.validity !== "undefined" ) {  
                return element.validity.badInput ? false : $element.val();  
            }  
  
            val = $element.val();  
            if ( typeof val === "string" ) {  
                return val.replace(/\r/g, "" );  
            }  
            return val;  
        },  
  
        // 执行验证，并更新错误元素列表errorList、错误文本列表errorMap、成功元素列表successList，以及this.submitted  
        check: function( element ) {  
            element = this.validationTargetFor( this.clean( element ) );//当前验证元素  
  
            var rules = $( element ).rules(),// 获取元素的验证规则  
                rulesCount = $.map( rules, function( n, i ) {// $.map由返回值拼接成新数组  
                    return i;  
                }).length,  
                dependencyMismatch = false,  
                val = this.elementValue( element ),//表单元素的值  
                result, method, rule;  
  
            for ( method in rules ) {  
                rule = { method: method, parameters: rules[ method ] };  
                try {  
  
                    result = $.validator.methods[ method ].call( this, val, element, rule.parameters );  
  
                    // 依赖条件不成立，跳过验证  
                    if ( result === "dependency-mismatch" && rulesCount === 1 ) {  
                        dependencyMismatch = true;  
                        continue;  
                    }  
                    dependencyMismatch = false;  
  
                    if ( result === "pending" ) {  
                        this.toHide = this.toHide.not( this.errorsFor( element ) );  
                        return;  
                    }  
  
                    if ( !result ) {  
                        this.formatAndAdd( element, rule );  
                        return false;  
                    }  
                } catch ( e ) {  
                    if ( this.settings.debug && window.console ) {  
                        console.log( "Exception occurred when checking element " + element.id + ", check the '" + rule.method + "' method.", e );  
                    }  
                    if ( e instanceof TypeError ) {  
                        e.message += ".  Exception occurred when checking element " + element.id + ", check the '" + rule.method + "' method.";  
                    }  
  
                    throw e;  
                }  
            }  
            if ( dependencyMismatch ) {  
                return;  
            }  
            if ( this.objectLength( rules ) ) {  
                this.successList.push( element );  
            }  
            return true;  
        },  
  
        // 返回data属性中设置的错误提示文本，形如data-msgRequired=""  
        customDataMessage: function( element, method ) {  
            return $( element ).data( "msg" + method.charAt( 0 ).toUpperCase() +  
                method.substring( 1 ).toLowerCase() ) || $( element ).data( "msg" );  
        },  
  
        // 返回js中设置的错误提示文本  
        customMessage: function( name, method ) {  
            var m = this.settings.messages[ name ];  
            return m && ( m.constructor === String ? m : m[ method ]);  
        },  
  
        // 从js配置文件、data属性、title属性中找到有值的第一条错误提示文本  
        findDefined: function() {  
            for ( var i = 0; i < arguments.length; i++) {  
                if ( arguments[ i ] !== undefined ) {  
                    return arguments[ i ];  
                }  
            }  
            return undefined;  
        },  
  
        // 返回错误文本  
        defaultMessage: function( element, method ) {  
            return this.findDefined(  
                this.customMessage( element.name, method ),  
                this.customDataMessage( element, method ),  
                // title is never undefined, so handle empty string as undefined  
                !this.settings.ignoreTitle && element.title || undefined,  
                $.validator.messages[ method ],  
                "<strong>Warning: No message defined for " + element.name + "</strong>"  
            );  
        },  
  
        // 填充并获得错误文本，errorList、errorMap、submitted中添加值  
        formatAndAdd: function( element, rule ) {  
            var message = this.defaultMessage( element, rule.method ),  
                theregex = /\$?\{(\d+)\}/g;  
            if ( typeof message === "function" ) {  
                // rule.paraments即{max:12}中的12  
                message = message.call( this, rule.parameters, element );  
            } else if ( theregex.test( message ) ) {  
                message = $.validator.format( message.replace( theregex, "{$1}" ), rule.parameters );  
            }  
            this.errorList.push({  
                message: message,  
                element: element,  
                method: rule.method  
            });  
  
            this.errorMap[ element.name ] = message;  
            this.submitted[ element.name ] = message;  
        },  
  
        // 向jquery对象中添加错误提示包裹元素  
        addWrapper: function( toToggle ) {  
            if ( this.settings.wrapper ) {  
                toToggle = toToggle.add( toToggle.parent( this.settings.wrapper ) );  
            }  
            return toToggle;  
        },  
  
        // 根据错误元素列表errorList执行highlight函数及showLabel函数显示错误文本，根据successList列表调用showLabel函数调用success回调  
        // 更新this.toShow已展示的错误提示文本元素，this.toHide更新隐藏的错误提示文本元素  
        // 错误提示文本元素集体隐藏后，再展示this.toShow中的错误提示文本元素  
        defaultShowErrors: function() {  
            var i, elements, error;  
            for ( i = 0; this.errorList[ i ]; i++ ) {  
                error = this.errorList[ i ];  
                if ( this.settings.highlight ) {  
                    this.settings.highlight.call( this, error.element, this.settings.errorClass, this.settings.validClass );  
                }  
                this.showLabel( error.element, error.message );  
            }  
            if ( this.errorList.length ) {  
                this.toShow = this.toShow.add( this.containers );  
            }  
            if ( this.settings.success ) {  
                for ( i = 0; this.successList[ i ]; i++ ) {  
                    this.showLabel( this.successList[ i ] );  
                }  
            }  
            if ( this.settings.unhighlight ) {  
                for ( i = 0, elements = this.validElements(); elements[ i ]; i++ ) {  
                    this.settings.unhighlight.call( this, elements[ i ], this.settings.errorClass, this.settings.validClass );  
                }  
            }  
            this.toHide = this.toHide.not( this.toShow );  
            this.hideErrors();  
            this.addWrapper( this.toShow ).show();  
        },  
  
        // 返回通过验证的元素列表  
        validElements: function() {  
            return this.currentElements.not( this.invalidElements() );  
        },  
  
        // 返回未通过验证的元素列表  
        invalidElements: function() {  
            return $( this.errorList ).map(function() {  
                return this.element;  
            });  
        },  
  
        // 验证成功或失败时都会调用showLabel函数，差别是验证成功时不会传入message函数  
        // 验证失败时更新错误提示文本内容，添加样式并显示，成功时执行success回调或添加样式，更新toShow列表  
        showLabel: function( element, message ) {  
            var place, group, errorID,  
                error = this.errorsFor( element ),// 找到提示文本元素  
                elementID = this.idOrName( element ),  
                describedBy = $( element ).attr( "aria-describedby" );  
            if ( error.length ) {  
                // 在html页面写就<label for="name">的前提下，更新样式及添加文案，忽略wrapper、labelContainer设置  
                error.removeClass( this.settings.validClass ).addClass( this.settings.errorClass );  
                error.html( message );  
            } else {  
                // 在html页面没有写就<label for="name">的前提下,创建并放置到相应位置  
                error = $( "<" + this.settings.errorElement + ">" )  
                    .attr( "id", elementID + "-error" )  
                    .addClass( this.settings.errorClass )  
                    .html( message || "" );  
  
                place = error;  
                if ( this.settings.wrapper ) {  
                    place = error.hide().show().wrap( "<" + this.settings.wrapper + "/>" ).parent();  
                }  
  
                if ( this.labelContainer.length ) {  
                    this.labelContainer.append( place );  
                } else if ( this.settings.errorPlacement ) {  
                    this.settings.errorPlacement( place, $( element ) );  
                } else {  
                    place.insertAfter( element );  
                }  
  
                // Link error back to the element  
                if ( error.is( "label" ) ) {  
                    // If the error is a label, then associate using 'for'  
                    error.attr( "for", elementID );  
  
                    // 错误提示文本元素的id号根据验证元素的id或name属性创建，当错误提示元素没有label包裹时，  
                    // 更新或创建验证元素的aria-describedby属性  
                } else if ( error.parents( "label[for='" + elementID + "']" ).length === 0 ) {  
                    // If the element is not a child of an associated label, then it's necessary  
                    // to explicitly apply aria-describedby  
  
                    errorID = error.attr( "id" ).replace( /(:|\.|\[|\]|\$)/g, "\\$1");  
                    // Respect existing non-error aria-describedby  
                    if ( !describedBy ) {  
                        describedBy = errorID;  
                    } else if ( !describedBy.match( new RegExp( "\\b" + errorID + "\\b" ) ) ) {  
                        // Add to end of list if not already present  
                        describedBy += " " + errorID;  
                    }  
                    $( element ).attr( "aria-describedby", describedBy );  
  
                    // If this element is grouped, then assign to all elements in the same group  
                    group = this.groups[ element.name ];  
                    if ( group ) {  
                        $.each( this.groups, function( name, testgroup ) {  
                            if ( testgroup === group ) {  
                                $( "[name='" + name + "']", this.currentForm )  
                                    .attr( "aria-describedby", error.attr( "id" ) );  
                            }  
                        });  
                    }  
                }  
            }  
            if ( !message && this.settings.success ) {  
                error.text( "" );  
                if ( typeof this.settings.success === "string" ) {  
                    error.addClass( this.settings.success );  
                } else {  
                    this.settings.success( error, element );  
                }  
            }  
            this.toShow = this.toShow.add( error );  
        },  
  
        // 在执行errors函数的基础上获取label[for='+name+']或者带有aria-describedby属性的错误提示元素;  
        errorsFor: function( element ) {  
            var name = this.idOrName( element ),  
                describer = $( element ).attr( "aria-describedby" ),  
                selector = "label[for='" + name + "'], label[for='" + name + "'] *";  
  
            // aria-describedby should directly reference the error element  
            if ( describer ) {  
                selector = selector + ", #" + describer.replace( /\s+/g, ", #" );  
            }  
            return this  
                .errors()  
                .filter( selector );  
        },  
  
        // 对一组表单元素（比如多个checkbox/radio、以及用户自定义的一组验证）验证取name属性，否则取id  
        idOrName: function( element ) {  
            return this.groups[ element.name ] || ( this.checkable( element ) ? element.name : element.id || element.name );  
        },  
  
        // 获得待验证元素的jquery对象，不包含radio、checkbox  
        validationTargetFor: function( element ) {  
  
            // If radio/checkbox, validate first element in group instead  
            if ( this.checkable( element ) ) {  
                element = this.findByName( element.name );  
            }  
  
            // Always apply ignore filter  
            return $( element ).not( this.settings.ignore )[ 0 ];  
        },  
  
        // 判断是否复选框或单选框  
        checkable: function( element ) {  
            return ( /radio|checkbox/i ).test( element.type );  
        },  
  
        // 通过Dom元素的name属性获取jquery对象  
        findByName: function( name ) {  
            return $( this.currentForm ).find( "[name='" + name + "']" );  
        },  
  
        // 判断选中的复选框、单选框、下拉框是否有值  
        getLength: function( value, element ) {  
            switch ( element.nodeName.toLowerCase() ) {  
            case "select":  
                return $( "option:selected", element ).length;  
            case "input":  
                if ( this.checkable( element ) ) {  
                    return this.findByName( element.name ).filter( ":checked" ).length;  
                }  
            }  
            return value.length;  
        },  
  
        // 执行验证前先判断依赖条件是否成立  
        depend: function( param, element ) {  
            return this.dependTypes[typeof param] ? this.dependTypes[typeof param]( param, element ) : true;  
        },  
  
        // 执行验证需要判断的依赖条件，传入string判断元素是否存在，如select:visible，传入函数当前Dom元素匹配某种条件  
        dependTypes: {  
            "boolean": function( param ) {  
                return param;  
            },  
            "string": function( param, element ) {  
                return !!$( param, element.form ).length;  
            },  
            "function": function( param, element ) {  
                return param( element );  
            }  
        },  
  
        // 元素没有值或者有值的复选框、单选框未被选中的情况下，返回"dependency-mismatch"  
        optional: function( element ) {  
            var val = this.elementValue( element );  
            return !$.validator.methods.required.call( this, val, element ) && "dependency-mismatch";  
        },  
  
        // this.pendingRequest自增1，发送远程验证的个数  
        startRequest: function( element ) {  
            if ( !this.pending[ element.name ] ) {  
                this.pendingRequest++;// 用来记录所有远程验证通过后再执行表单  
                this.pending[ element.name ] = true;  
            }  
        },  
  
        // 远程验证全通过后，提交表单或触发invalid-form事件  
        stopRequest: function( element, valid ) {  
            this.pendingRequest--;  
            // sometimes synchronization fails, make sure pendingRequest is never < 0  
            if ( this.pendingRequest < 0 ) {  
                this.pendingRequest = 0;  
            }  
            delete this.pending[ element.name ];  
            if ( valid && this.pendingRequest === 0 && this.formSubmitted && this.form() ) {  
                $( this.currentForm ).submit();  
                this.formSubmitted = false;  
            } else if (!valid && this.pendingRequest === 0 && this.formSubmitted ) {  
                $( this.currentForm ).triggerHandler( "invalid-form", [ this ]);  
                this.formSubmitted = false;  
            }  
        },  
  
        // 初始化记录元素前一次验证的值和结果、提示文本，当前值和前一次值相等时，直接返回上一次结果,  
        // 否则更新data-previousValue属性中的old为当前值，并更新验证结果valid,  
        // message、orginalMessage初始设置配置项中的remote提示文本，其中message可能被更新为后台返回的错误文本,  
        // 或者获取前一次的验证信息  
        previousValue: function( element ) {  
            return $.data( element, "previousValue" ) || $.data( element, "previousValue", {  
                old: null,  
                valid: true,  
                message: this.defaultMessage( element, "remote" )  
            });  
        },  
  
        // 重置表单，解绑.validate事件，移除属性中的实例  
        destroy: function() {  
            this.resetForm();  
  
            $( this.currentForm )  
                .off( ".validate" )  
                .removeData( "validator" );  
        }  
  
    },  
  
    // classRuleSettings默认可以添加到Dom元素类中的验证规整，包括required、digits等;  
    classRuleSettings: {  
        required: { required: true },  
        email: { email: true },  
        url: { url: true },  
        date: { date: true },  
        dateISO: { dateISO: true },  
        number: { number: true },  
        digits: { digits: true },  
        creditcard: { creditcard: true }  
    },  
  
    // addClassRules添加可以向Dom元素添加的样式规则类;  
    addClassRules: function( className, rules ) {  
        if ( className.constructor === String ) {  
            this.classRuleSettings[ className ] = rules;  
        } else {  
            $.extend( this.classRuleSettings, className );  
        }  
    },  
  
    // classRules由Dom元素的样式规则类获取真实的验证规则;  
    classRules: function( element ) {  
        var rules = {},  
            classes = $( element ).attr( "class" );  
  
        if ( classes ) {  
            $.each( classes.split( " " ), function() {  
                if ( this in $.validator.classRuleSettings ) {  
                    $.extend( rules, $.validator.classRuleSettings[ this ]);  
                }  
            });  
        }  
        return rules;  
    },  
  
    // 向rules对象中添加规则，一般是{method:value}，value一定情形下会转化成数值型  
    // 当value为否时，type===method且type!=="range"的情形下，向rules中添加{method:true}  
    normalizeAttributeRule: function( rules, type, method, value ) {  
  
        // convert the value to a number for number inputs, and for text for backwards compability  
        // allows type="date" and others to be compared as strings  
        if ( /min|max/.test( method ) && ( type === null || /number|range|text/.test( type ) ) ) {  
            value = Number( value );  
  
            // Support Opera Mini, which returns NaN for undefined minlength  
            if ( isNaN( value ) ) {  
                value = undefined;  
            }  
        }  
  
        if ( value || value === 0 ) {  
            rules[ method ] = value;  
        } else if ( type === method && type !== "range" ) {  
  
            // exception: the jquery validate 'range' method  
            // does not test for the html5 'range' type  
            rules[ method ] = true;  
        }  
    },  
  
    // attributeRules由Dom元素的验证规则同名方法属性获取验证规则，type属性简化验证规则设置成true形式，调用normalizeAttributeRule实现;  
    attributeRules: function( element ) {  
        var rules = {},  
            $element = $( element ),  
            type = element.getAttribute( "type" ),//getAttribute获取元素属性  
            method, value;  
  
        for ( method in $.validator.methods ) {  
  
            // support for <input required> in both html5 and older browsers  
            if ( method === "required" ) {  
                value = element.getAttribute( method );  
  
                // Some browsers return an empty string for the required attribute  
                // and non-HTML5 browsers might have required="" markup  
                if ( value === "" ) {  
                    value = true;  
                }  
  
                // force non-HTML5 browsers to return bool  
                value = !!value;// !!转换成布尔型  
            } else {  
                value = $element.attr( method );  
            }  
  
            this.normalizeAttributeRule( rules, type, method, value );  
        }  
  
        // maxlength may be returned as -1, 2147483647 ( IE ) and 524288 ( safari ) for text inputs  
        if ( rules.maxlength && /-1|2147483647|524288/.test( rules.maxlength ) ) {  
            delete rules.maxlength;  
        }  
  
        return rules;  
    },  
  
    // dataRules由Dom元素的形如data-ruleRequired属性获取验证规则，调用normalizeAttributeRule实现;  
    dataRules: function( element ) {  
        var rules = {},  
            $element = $( element ),  
            type = element.getAttribute( "type" ),  
            method, value;  
  
        for ( method in $.validator.methods ) {  
            value = $element.data( "rule" + method.charAt( 0 ).toUpperCase() + method.substring( 1 ).toLowerCase() );  
            this.normalizeAttributeRule( rules, type, method, value );  
        }  
        return rules;  
    },  
  
    // 获取js中设置的验证规则  
    staticRules: function( element ) {  
        var rules = {},  
            validator = $.data( element.form, "validator" );  
  
        if ( validator.settings.rules ) {  
            rules = $.validator.normalizeRule( validator.settings.rules[ element.name ] ) || {};  
        }  
        return rules;  
    },  
  
    // 将设置了验证条件depends、函数function(element)、最小最大值的验证规则转换成key-value键值对形式;  
    normalizeRules: function( rules, element ) {  
        $.each( rules, function( prop, val ) {  
  
            if ( val === false ) {  
                delete rules[ prop ];  
                return;  
            }  
            /** 
             * {required:{ 
             *      param:str/function,     //具体的验证规则，假使写入函数，函数的参数是当前dom元素 
             *      depends:str/fn  //设置某元素存在才执行验证，或者依赖某种条件成立再执行验证 
             * }} 
             */  
            if ( val.param || val.depends ) {  
                var keepRule = true;  
                switch ( typeof val.depends ) {  
                case "string":  
                    keepRule = !!$( val.depends, element.form ).length;  
                    break;  
                case "function":  
                    keepRule = val.depends.call( element, element );  
                    break;  
                }  
                if ( keepRule ) {  
                    rules[ prop ] = val.param !== undefined ? val.param : true;  
                } else {  
                    delete rules[ prop ];  
                }  
            }  
        });  
  
        $.each( rules, function( rule, parameter ) {  
            rules[ rule ] = $.isFunction( parameter ) ? parameter( element ) : parameter;  
        });  
  
        $.each([ "minlength", "maxlength" ], function() {  
            if ( rules[ this ] ) {// this指代数组对象的当前值  
                rules[ this ] = Number( rules[ this ] );  
            }  
        });  
        // 设置range可以是数组形式[min,max]，或者字符串形式"min,max"  
        $.each([ "rangelength", "range" ], function() {  
            var parts;  
            if ( rules[ this ] ) {  
                if ( $.isArray( rules[ this ] ) ) {  
                    rules[ this ] = [ Number( rules[ this ][ 0 ]), Number( rules[ this ][ 1 ] ) ];  
                } else if ( typeof rules[ this ] === "string" ) {  
                    parts = rules[ this ].replace(/[\[\]]/g, "" ).split( /[\s,]+/ );  
                    rules[ this ] = [ Number( parts[ 0 ]), Number( parts[ 1 ] ) ];  
                }  
            }  
        });  
        //设置min，max的验证规则自动转化成range形式  
        if ( $.validator.autoCreateRanges ) {  
            if ( rules.min != null && rules.max != null ) {  
                rules.range = [ rules.min, rules.max ];  
                delete rules.min;  
                delete rules.max;  
            }  
            if ( rules.minlength != null && rules.maxlength != null ) {  
                rules.rangelength = [ rules.minlength, rules.maxlength ];  
                delete rules.minlength;  
                delete rules.maxlength;  
            }  
        }  
  
        return rules;  
    },  
  
    // 将字符串形式的验证规则转换成对象形式，element:"required"转换成{"required":true};  
    normalizeRule: function( data ) {  
        if ( typeof data === "string" ) {  
            var transformed = {};  
            $.each( data.split( /\s/ ), function() {  
                transformed[ this ] = true;  
            });  
            data = transformed;  
        }  
        return data;  
    },  
  
    // 添加验证规则，更新$.validator.methods、$.validator.messages配置  
    // 通过$.validator.addClassRules向元素添加样式规则类执行验证  
    addMethod: function( name, method, message ) {  
        $.validator.methods[ name ] = method;  
        $.validator.messages[ name ] = message !== undefined ? message : $.validator.messages[ name ];  
        if ( method.length < 3 ) {  
            $.validator.addClassRules( name, $.validator.normalizeRule( name ) );  
        }  
    },  
  
    // 验证逻辑  
    // methods[method].call(this,val,element,rule.parameters),  
    //      this为validator构造函数,  
    //      val表单元素的值，element表单元素，rule.parameters验证规则的值，如{min:3}中的3;  
    // required非空验证，其他验证以此为依赖条件;  
    // email验证邮箱;  
    // url验证链接;  
    // date验证日期;  
    // dateISO验证ISO日期;  
    // number验证带-、.的数字;  
    // digits验证纯数字;  
    // creditcard验证信用卡号;  
    // minlength、maxlength、rangelength验证单选框、复选框、下拉框选中的个数;  
    // min、max、range最小最大值验证;  
    // equalto验证元素的值和另一个元素等同;  
    methods: {  
  
        // {required:true}、{required:function(){return $(anotherEle).val()<12}}依赖条件  
        required: function( value, element, param ) {  
            // 当param传入函数时，判断元素执行验证前需要满足的依赖条件是否成立   
            if ( !this.depend( param, element ) ) {  
                return "dependency-mismatch";  
            }  
            if ( element.nodeName.toLowerCase() === "select" ) {  
                // could be an array for select-multiple or a string, both are fine this way  
                var val = $( element ).val();  
                return val && val.length > 0;  
            }  
            if ( this.checkable( element ) ) {  
                return this.getLength( value, element ) > 0;  
            }  
            return value.length > 0;  
        },  
  
        // 先进行非空验证（当元素有值时this.optional返回否值），再执行正则匹配  
        email: function( value, element ) {  
            return this.optional( element ) || /^[a-zA-Z0-9.!#$%&'*+\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/.test( value );  
        },  
        url: function( value, element ) {  
            return this.optional( element ) || /^(?:(?:(?:https?|ftp):)?\/\/)(?:\S+(?::\S*)?@)?(?:(?!(?:10|127)(?:\.\d{1,3}){3})(?!(?:169\.254|192\.168)(?:\.\d{1,3}){2})(?!172\.(?:1[6-9]|2\d|3[0-1])(?:\.\d{1,3}){2})(?:[1-9]\d?|1\d\d|2[01]\d|22[0-3])(?:\.(?:1?\d{1,2}|2[0-4]\d|25[0-5])){2}(?:\.(?:[1-9]\d?|1\d\d|2[0-4]\d|25[0-4]))|(?:(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)(?:\.(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)*(?:\.(?:[a-z\u00a1-\uffff]{2,})).?)(?::\d{2,5})?(?:[/?#]\S*)?$/i.test( value );  
        },  
        date: function( value, element ) {  
            return this.optional( element ) || !/Invalid|NaN/.test( new Date( value ).toString() );  
        },  
        dateISO: function( value, element ) {  
            return this.optional( element ) || /^\d{4}[\/\-](0?[1-9]|1[012])[\/\-](0?[1-9]|[12][0-9]|3[01])$/.test( value );  
        },  
        number: function( value, element ) {  
            return this.optional( element ) || /^(?:-?\d+|-?\d{1,3}(?:,\d{3})+)?(?:\.\d+)?$/.test( value );  
        },  
        digits: function( value, element ) {  
            return this.optional( element ) || /^\d+$/.test( value );  
        },  
  
        // http://jqueryvalidation.org/creditcard-method/  
        // based on http://en.wikipedia.org/wiki/Luhn_algorithm  
        creditcard: function( value, element ) {  
            if ( this.optional( element ) ) {  
                return "dependency-mismatch";  
            }  
  
            if ( /[^0-9 \-]+/.test( value ) ) {  
                return false;  
            }  
            var nCheck = 0,  
                nDigit = 0,  
                bEven = false,  
                n, cDigit;  
  
            value = value.replace( /\D/g, "" );  
  
            // http://developer.ean.com/general_info/Valid_Credit_Card_Types  
            // 信用卡号符合哪种机制？？？  
            if ( value.length < 13 || value.length > 19 ) {  
                return false;  
            }  
  
            for ( n = value.length - 1; n >= 0; n--) {  
                cDigit = value.charAt( n );  
                nDigit = parseInt( cDigit, 10 );  
                if ( bEven ) {  
                    if ( ( nDigit *= 2 ) > 9 ) {  
                        nDigit -= 9;  
                    }  
                }  
                nCheck += nDigit;  
                bEven = !bEven;  
            }  
  
            return ( nCheck % 10 ) === 0;  
        },  
  
        // 验证最小最大长度，传入数组取数组长度，传入非数组判断单选框、复选框、多选框选中了几个  
        minlength: function( value, element, param ) {  
            var length = $.isArray( value ) ? value.length : this.getLength( value, element );  
            return this.optional( element ) || length >= param;  
        },  
        maxlength: function( value, element, param ) {  
            var length = $.isArray( value ) ? value.length : this.getLength( value, element );  
            return this.optional( element ) || length <= param;  
        },  
        rangelength: function( value, element, param ) {  
            var length = $.isArray( value ) ? value.length : this.getLength( value, element );  
            return this.optional( element ) || ( length >= param[ 0 ] && length <= param[ 1 ] );  
        },  
  
        // 验证最小最大值  
        min: function( value, element, param ) {  
            return this.optional( element ) || value >= param;  
        },  
        max: function( value, element, param ) {  
            return this.optional( element ) || value <= param;  
        },  
        range: function( value, element, param ) {  
            return this.optional( element ) || ( value >= param[ 0 ] && value <= param[ 1 ] );  
        },  
  
        // http://jqueryvalidation.org/equalTo-method/  
        equalTo: function( value, element, param ) {  
            // bind to the blur event of the target in order to revalidate whenever the target field is updated  
            // TODO find a way to bind the event just once, avoiding the unbind-rebind overhead  
            var target = $( param );  
            if ( this.settings.onfocusout ) {  
                target.off( ".validate-equalTo" ).on( "blur.validate-equalTo", function() {  
                    $( element ).valid();// 执行验证  
                });  
            }  
            return value === target.val();  
        },  
  
        // http://jqueryvalidation.org/remote-method/  
        // rules:{  
        //      name:url/{url:url,data:data,type:type} // data默认取验证元素的值  
        // }  
        remote: function( value, element, param ) {  
            if ( this.optional( element ) ) {  
                return "dependency-mismatch";  
            }  
  
            var previous = this.previousValue( element ),// 获取上一次的验证结果、值。提示文本  
                validator, data;  
  
            if (!this.settings.messages[ element.name ] ) {  
                this.settings.messages[ element.name ] = {};  
            }  
            previous.originalMessage = this.settings.messages[ element.name ].remote;  
            this.settings.messages[ element.name ].remote = previous.message;  
  
            param = typeof param === "string" && { url: param } || param;  
  
            // 当前验证与上一次情形相同，返回前一次验证结果  
            if ( previous.old === value ) {  
                return previous.valid;  
            }  
  
            // 更新元素data数据中缓存的前一次验证  
            previous.old = value;  
            validator = this;  
            this.startRequest( element );  
            data = {};  
            data[ element.name ] = value;// data默认取验证元素的值  
            $.ajax( $.extend( true, {  
                mode: "abort",  
                port: "validate" + element.name,// port和元素name相关，发送ajax后pendingRequests[port]标记为有值，阻止同一个元素发送两次ajax  
                dataType: "json",  
                data: data,// data默认取验证元素的值，可能被param中的data属性替换  
                context: validator.currentForm,  
                success: function( response ) {  
                    var valid = response === true || response === "true",  
                        errors, message, submitted;  
  
                    validator.settings.messages[ element.name ].remote = previous.originalMessage;  
                    if ( valid ) {  
                        submitted = validator.formSubmitted;  
                        validator.prepareElement( element );  
                        validator.formSubmitted = submitted;  
                        validator.successList.push( element );  
                        delete validator.invalid[ element.name ];  
                        validator.showErrors();// 调用showDefaultErrors中的success回调  
                    } else {  
                        errors = {};  
                        message = response || validator.defaultMessage( element, "remote" );  
                        errors[ element.name ] = previous.message = $.isFunction( message ) ? message( value ) : message;  
                        validator.invalid[ element.name ] = true;  
                        validator.showErrors( errors );  
                    }  
                    previous.valid = valid;// 同时更新元素data属性中的验证结果？？？  
                    validator.stopRequest( element, valid );  
                }  
            }, param ) );  
            return "pending";// 异步提示正在发送ajax请求  
        }  
    }  
  
});  
  
  
  
  
  
//
//
//
//
//$.validator.addMethod("specialCheck",function(va,element,params){
//	
//	
//	for (var i =0 ;i<va.length;i++) {
//		if(va[i]==="+"||va[i]==="$"||va[i]==="-"||va[i]==="*"||va[i]==="&"||va[i]==="^"||va[i]==="@"||va[i]==="#"||va[i]===" "){
//			return false;
//		}
//	}
//	       return true;
//
//},"不能包含特殊字符");
//
//
  
  
  
  
  
var pendingRequests = {},  
    ajax;  
// Use a prefilter if available (1.5+)  
// 为ajax方法添加abort、port标志，阻止同一验证元素两次发送ajax  
if ( $.ajaxPrefilter ) {  
    // ajaxPrefilter发送前的预处理，setting请求的所有参数，xhr经过jquery封装的XMLHttpRequest对象  
    $.ajaxPrefilter(function( settings, _, xhr ) {  
        var port = settings.port;  
        if ( settings.mode === "abort" ) {  
            if ( pendingRequests[port] ) {  
                pendingRequests[port].abort();  
            }  
            pendingRequests[port] = xhr;  
        }  
    });  
} else {  
    ajax = $.ajax;  
    // 在匿名函数中改写$.ajax，不改变其他js文件的$.ajax方法  
    $.ajax = function( settings ) {  
        var mode = ( "mode" in settings ? settings : $.ajaxSettings ).mode,  
            port = ( "port" in settings ? settings : $.ajaxSettings ).port;  
        if ( mode === "abort" ) {  
            if ( pendingRequests[port] ) {  
                pendingRequests[port].abort();// abort()用来终止ajax请求，还是会执行success回调，返回值为空  
            }  
            // port和元素name相关，发送ajax后pendingRequests[port]标记为有值，阻止同一个元素发送两次ajax  
            pendingRequests[port] = ajax.apply(this, arguments);  
            return pendingRequests[port];  
        }  
        return ajax.apply(this, arguments);  
    };  
}  
  
}));  


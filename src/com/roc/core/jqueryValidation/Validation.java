package com.roc.core.jqueryValidation;

import com.roc.core.Utils.*;
import com.roc.core.tags.ActionInfo;
import org.jdom.*;

import java.util.*;

/**
 * User: rocwu
 * Date: 2016/08/14
 * Time: 12:31
 * Desc: 前端验证类
 */
public class Validation {

    /**
     * action=>jqueryValidation
     */
    private static Map<String, Map<String, Field>> validationMap = new HashMap<>();

    private Validation() {}

    /**
     * 由action信息产生前端校验js代码
     * bootstrap validate
     * @param actionInfo
     * @return
     */
    public static String[] generateValidationJs(final ActionInfo actionInfo) {
        StringBuilder sbReadyProcessFunc = new StringBuilder();
        Map<String, Field> fieldValidatorMap = getValidations(actionInfo);
        StringBuilder sb = new StringBuilder();
        sb.append("fields: {\n");
        for (String fieldName :fieldValidatorMap.keySet()) {
            Field field = fieldValidatorMap.get(fieldName);
            sb.append("\t'"+fieldName+"': {\n");
            sb.append("\t\tmessage: 'The "+fieldName+" is not valid',\n");
            sb.append("\t\tvalidators: {\n");
            for (FieldValidator fieldValidator : field.getFieldValidatorList()) {
                switch (fieldValidator.type) {
                    case REQUIRED:
                    case REQUIREDSTRING:
                        sb.append("\t\t\tnotEmpty: {\n");
                        if (!StringUtil.isEmpty(fieldValidator.message))
                            sb.append("\t\t\t\tmessage: '").append(fieldValidator.message).append("',\n");
                        for (ValidatorParam param : fieldValidator.paramList) {
                            if ("trim".equals(param.getName())) {
                                sb.append("\t\t\t\ttrim: ").append(param.getValue()).append(",\n");
                            }
                        }
                        sb.append("\t\t\t},\n");
                        break;
                    case STRINGLENGTH:
                        sb.append("\t\t\tstringLength: {\n");
                        int maxLength = Integer.MAX_VALUE;
                        int minLength = 0;
                        for (ValidatorParam param : fieldValidator.paramList) {
                            if ("minLength".equals(param.getName()))
                                minLength = Integer.parseInt(param.getValue());
                            else if ("maxLength".equals(param.getName()))
                                maxLength = Integer.parseInt(param.getValue());
                            else if ("trim".equals(param.getName())) {
                                sb.append("\t\t\t\ttrim: ").append(param.getValue()).append(",\n");
                            }
                        }
                        if (!StringUtil.isEmpty(fieldValidator.message))
                            sb.append("\t\t\t\tmessage: '").append(fieldValidator.message).append("',\n");
                        sb.append("\t\t\t\tmin: ").append(minLength).append(",\n");
                        sb.append("\t\t\t\tmax: ").append(maxLength).append(",\n");
                        sb.append("\t\t\t},\n");
                        break;
                    case REGEX:
                        sb.append("\t\t\tregexp: {\n");
                        if (!StringUtil.isEmpty(fieldValidator.message))
                            sb.append("\t\t\t\tmessage: '").append(fieldValidator.message).append("',\n");
                        for (ValidatorParam param : fieldValidator.paramList) {
                            if ("regexp".equals(param.getName()))
                                sb.append("\t\t\t\tregexp: '"+param.getValue()+"',\n");
                            else if ("trim".equals(param.getName())) {
                                sb.append("\t\t\t\ttrim: ").append(param.getValue()).append(",\n");
                            }
                        }
                        sb.append("\t\t\t},\n");
                        break;
                    case EMAIL:
                        sb.append("\t\t\temailAddress: {\n");
                        if (!StringUtil.isEmpty(fieldValidator.message))
                            sb.append("\t\t\t\tmessage: '").append(fieldValidator.message).append("',\n");
                        //TODO:trim,regexp
                        sb.append("\t\t\t},\n");
                        break;
                    case URL:
                        sb.append("\t\t\turi: {\n");
                        if (!StringUtil.isEmpty(fieldValidator.message))
                            sb.append("\t\t\t\tmessage: '").append(fieldValidator.message).append("',\n");
                        //TODO:trim,regexp
                        sb.append("\t\t\t},\n");
                        break;
                    case INT:
                    case LONG:
                    case SHORT:
                    case DOUBLE:
                        if (!StringUtil.isEmpty(fieldValidator.message))
                            sb.append("\t\t\t\tmessage: '").append(fieldValidator.message).append("',\n");
                        for (ValidatorParam param : fieldValidator.paramList) {
                            if ("min".equals(param.getName()))
                                sb.append("\t\t\t\tmin: ").append(param.getValue()).append(",\n");
                            else if ("max".equals(param.getName()))
                                sb.append("\t\t\t\tmax: ").append(param.getValue()).append(",\n");
                        }
                        break;
                    case DATE:
                        //TODO:
                        break;
                    case CAPTCHA:
                        sb.append("\t\t\t\tcallback: {\n");
                        if (!StringUtil.isEmpty(fieldValidator.message))
                        sb.append("\t\t\t\t\tmessage: '").append(fieldValidator.message).append("',\n");
                        sb.append("\t\t\t\t\tcallback: function(value, validator) {\n");
                        sb.append("\t\t\t\t\tvar ret = false;\n");
                        sb.append("\t\t\t\t\t\thtmlobj=$.ajax({\n");
                        sb.append("\t\t\t\t\t\t\turl:\"/captchaVerify.action?v=\"+$(\"input[name='"+fieldName+"-v']\").val()+\"&captcha=\"+$(\"input[name='"+fieldName+"']\").val()+\"&name="+fieldName+"\",\n");
                        sb.append("\t\t\t\t\t\t\tasync:false,\n");
                        sb.append("\t\t\t\t\t\t\ttype:\"json\",\n");
                        sb.append("\t\t\t\t\t\t\tsuccess : function(data) {\n");
                        sb.append("\t\t\t\t\t\t\t\tret = JSON.parse(data);\n");
                        sb.append("\t\t\t\t\t\t\t\tret = ret.success;\n");
                        sb.append("\t\t\t\t\t\t\t}\n");
                        sb.append("\t\t\t\t\t\t});\n");
                        sb.append("\t\t\t\t\t\treturn ret;\n");
                        sb.append("\t\t\t\t\t},\n");
                        sb.append("\t\t\t\t}\n");

                        sbReadyProcessFunc.append("$(\"img[name='captcha-img']\").click(function () {\n");
                        sbReadyProcessFunc.append("\tvar v = new Date().getTime()%1000;\n");
                        sbReadyProcessFunc.append("\t$(\"img[name='captcha-img']\").attr('src', '/captcha.action?v='+v+'&name="+fieldName+"');\n");
                        sbReadyProcessFunc.append("\t$(\"input[name='captcha-v']\").val(v);\n");
                        sbReadyProcessFunc.append("\t$(\"input[name='captcha']\").val('');\n");
                        sbReadyProcessFunc.append("});\n");
                        break;
                    default:
                        break;
                }
            }
            sb.append("\t\t}\n");
            sb.append("\t},\n");
        }
        sb.append("}\n");
        return new String[] {sbReadyProcessFunc.toString(), sb.toString()};
    }

    /**
     * 由action信息获取校验字段
     * @param actionInfo
     * @return
     */
    public static Map<String, Field> getValidations(final ActionInfo actionInfo) {
        String key = actionInfo.getClassType().getName()+"-"+actionInfo.getMethodName();
        if (validationMap.containsKey(key)) {
            return validationMap.get(key);
        }
        String configFile = getValidationConfigFile(actionInfo);
        Map<String, Field> validationConfig = parseValidationConfigFile(configFile);
        validationMap.put(key, validationConfig);
        return validationConfig;
    }

    /**
     * 获取表单验证配置文件
     * @param actionInfo
     * @return
     */
    private static String getValidationConfigFile(final ActionInfo actionInfo) {
        String folderPath = actionInfo.getClassType().getResource("").getFile();
        String classSimpleName = actionInfo.getClassType().getSimpleName();
        String methodName = actionInfo.getMethodName();
        String configFilePath = folderPath+"/"+classSimpleName+"-validation.xml";
        if (!FileUtil.exists(configFilePath)) {
            configFilePath = folderPath+"/"+classSimpleName+"-"+methodName+"-validation.xml";
            if (!FileUtil.exists(configFilePath)) configFilePath = null;
        }

        return configFilePath;
    }

    /**
     * 解析
     * @param configFilePath
     * @return
     */
    private static Map<String, Field> parseValidationConfigFile(String configFilePath) {
        Map<String, Field> map = new LinkedHashMap<>();
        Document document = null;
        try {
            document = XmlUtil.readDocument(configFilePath);
            Element root = document.getRootElement();
            List<Element> fieldList = root.getChildren("field");
            // 遍历每个字段
            for (Element fieldElement : fieldList) {
                String fieldName = fieldElement.getAttributeValue("name");
                List<Element> validatorList = fieldElement.getChildren("field-validator");
                Field field = createFieldValidator(validatorList);
                field.setName(fieldName);
                map.put(fieldName, field);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    private static Field createFieldValidator(List<Element> validatorList ) {
        Field field = new Field();
        for (Element validatorElement : validatorList) {
            FieldValidator fieldValidator = new FieldValidator();
            String type = validatorElement.getAttributeValue("type");
            if (type.equals("required")) {
                fieldValidator.type = ValidatorType.REQUIRED;
            } else if(type.equals("requiredstring")) {
                fieldValidator.type = ValidatorType.REQUIREDSTRING;
            } else if(type.equals("stringlength")) {
                fieldValidator.type = ValidatorType.STRINGLENGTH;
            } else if(type.equals("regex")) {
                fieldValidator.type = ValidatorType.REGEX;
            } else if(type.equals("email")) {
                fieldValidator.type = ValidatorType.EMAIL;
            } else if(type.equals("url")) {
                fieldValidator.type = ValidatorType.URL;
            } else if(type.equals("int")) {
                fieldValidator.type = ValidatorType.INT;
            } else if(type.equals("long")) {
                fieldValidator.type = ValidatorType.LONG;
            } else if(type.equals("short")) {
                fieldValidator.type = ValidatorType.SHORT;
            } else if(type.equals("double")) {
                fieldValidator.type = ValidatorType.DOUBLE;
            } else if(type.equals("date")) {
                fieldValidator.type = ValidatorType.DATE;
            } else if (type.equals("captcha")) {
                fieldValidator.type = ValidatorType.CAPTCHA;
            }
            fieldValidator.message = validatorElement.getChild("message").getValue();
            List<Element> paramList = validatorElement.getChildren("param");
            for (Element param : paramList) {
                ValidatorParam validatorParam = new ValidatorParam();
                validatorParam.setName(param.getAttributeValue("name"));
                validatorParam.setValue(param.getValue());
                fieldValidator.paramList.add(validatorParam);
            }
            field.getFieldValidatorList().add(fieldValidator);
        }
        return field;
    }
}

package com.tmall.wireless.virtualviewdemo.preview;

import java.io.Serializable;
import java.util.List;

public class JsonData implements Serializable {


    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable {
        /**
         * _id : 5cce8b64bb67ef22c83df46b
         * json : {
         "name": "周学彬",
         "imageUrl": "http://192.168.2.4:8091/imgs/houzi.png",
         "status": "已a递"
         }
         * name : SINGLETEXT
         * __v : 0
         */

        private String json;
        private String name;


        public String getJson() {
            return json;
        }

        public void setJson(String json) {
            this.json = json;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

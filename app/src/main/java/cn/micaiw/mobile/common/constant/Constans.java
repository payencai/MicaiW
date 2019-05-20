package cn.micaiw.mobile.common.constant;

import android.os.Environment;

/**
 * Created by Administrator on 2018/2/8 0008.
 */

public class Constans {
    public final static boolean DEMO_MODE = true;  //true:DEBUG,false:RELEASE

    public final static String CourseModal = "CourseModal";        //ORDERID

    //缓存文件名
    public static final String CONFIGFILE = "SP_CONFIGFILE";
    public static final String ROOT_DIR = "gaotezhipei";
    public static final String LOCATION_ERROR_FILE = Environment.getExternalStorageDirectory() + "/" + ROOT_DIR + "/Error/";//异常信息文件

    /**
     * 培训课程模式
     * @author simon
     */
    public enum COURSE_MODAL {
        ONLINE(1), OFFLINE(2);
        private int index;

        COURSE_MODAL(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public static String[] getNameArray(int index) {
            switch (index) {
                case 1 :
                    return new String[] {"线上课程"};
                case 2 :
                    return new String[] {"线下课程"};
            }
            return null;
        }
    };

    public static enum DATABASE {
        DATABASE_DATA {
            public String toString() {
                return "gaotezhipei.db";
            }
        }
    }

    public static enum DATABASE_SQL {
        DATABASE_DATA_SQL {
            public String toString() {
                return "create_data.sql";
            }
        },
    }

    public static enum DATA_DB_TABLES {
        TBL_EXPRESS_COMPANY {
            public String toString() {
                return "tbl_express_company";
            }
        },
    }

//CREATE TABLE "location" (id integer NOT NULL PRIMARY KEY UNIQUE,area_code integer,area_name text,is_deleted integer);

    public static enum TBL_EXPRESS_COMPANY_COLUMNS {
        ID {
            public String toString() {
                return "id";
            }
        },
        CODE {
            public String toString() {
                return "code";
            }
        },
        NAME {
            public String toString() {
                return "name";
            }
        },
        PIC_KEY {
            public String toString() {
                return "pic_key";
            }
        },

        IS_CANCEL {
            public String toString() {
                return "is_cancel";
            }
        },
    }
}

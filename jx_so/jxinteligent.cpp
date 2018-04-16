#include     <stdio.h>      /*标准输入输出定义*/
#include     <stdlib.h>     /*标准函数库定义*/
#include     <unistd.h>     /*Unix 标准函数定义*/
#include     <sys/types.h> 
#include     <sys/stat.h>  
#include     <termios.h>    /*POSIX 终端控制定义*/
#include     <errno.h>      /*错误号定义*/
#include     <string.h>       /*字符串功能函数*/
#include 	 <sys/types.h>
#include     <fcntl.h>      /*文件控制定义*/



#ifdef LOG_TAG
#undef LOG_TAG
#endif
#define LOG_TAG "JXSO"
#define MAX_BUFFER_SIZE 255
#define MAX_LEVEL_NUM 5

#include "inline/Log.h"
#include "android_serialport_api_SerialPort.h"
enum{
	E_CMD_0 = 0xC0,   //common order code
	E_CMD_1 = 0xC1,   //set change mode
	E_CMD_2 = 0xC2,   //set open/close water
	E_CMD_3 = 0xC3,   //set filter element lift
	E_CMD_4 = 0xC4,   //set ver swith
	E_CMD_8 = 0xC8,   //set time
	E_CMD_MAX
};

typedef enum{
	E_OP_0 = 0x30,    //read machine param
	E_OP_1 = 0x50,    //add usage flow
	E_OP_2 = 0x51,     //set usage day
	E_OP_3 = 0x60,    //set change mode
	E_OP_4 = 0x70,    //set open/close water
	E_OP_5 = 0x71,    //set open/close water
	E_OP_6 = 0x80,    //set filter param
	E_OP_7 = 0x90,    //set ver open
	E_OP_8 = 0xA0,
	E_OP_9 = 0xA1,    //response status
	E_OP_A = 0xA2,    //machine status
	E_OP_B = 0xA3,    //user water status
	E_OP_F = 0xAA,    //water machine status
	E_OP_MAX
}tty_op_code_enum; 

enum{stat_code_0 = 0xAA, stat_code_1 = 0x55};

typedef enum{
	E_ORDER_CODE = 0,//receive order code
	E_CHARGE_CODE,
	E_PPF_CODE,
	E_BCARBON_CODE,   //keli carbon
	E_LCARBON_CODE,   //compress carbon
	E_RO_CODE,				//Reverse Osmosis
	E_TT3_CODE,				//后置活性碳，改善口质
	E_WAF_CODE,				//Weak alkali filter
	E_MAX_CODE
}tty_water_code_enum;

//读取参数，计费设置，增加流量，增加天数
typedef struct{
	unsigned char  fix_lcode;
	unsigned char  fix_hcode;//fixed mode 2byte
	unsigned char  cmd_code;//general command code
	unsigned char  op_code;  //operator code
	unsigned char  h_data;
	unsigned char  l_data;   //
	unsigned char  check_sum;
	unsigned char  check_mod; 
}to_tty_com_oport_st;

typedef struct{
	unsigned char  fix_hcode;
	unsigned char  fix_lcode;//fixed mode 2byte
	unsigned char  cmd_code;//general command code
	unsigned char  data_0;
	unsigned char  data_1;   //
	unsigned char  data_2;   //
	unsigned char  check_sum;
	unsigned char  check_mod; 
}to_tty_com_oport2_st;


//filter life
typedef struct{
	unsigned char  fix_lcode;
	unsigned char  fix_hcode;//fixed mode 2byte
	unsigned char  cmd_code;//general command code
	unsigned char  op_code;  //operator code
	unsigned char  flag_code;
	unsigned char  h_data;
	unsigned char  l_data;   //
	unsigned char  check_sum;
	unsigned char  check_mod; 
}to_tty_special_oport_st;


//cmd :C1
typedef struct{
	unsigned char  fix_lcode;
	unsigned char  fix_hcode;//fixed mode 2byte

	unsigned char  cmd_code;//C1
	unsigned char  year;     //operator code
	unsigned char  month;    //
	unsigned char  day;
	unsigned char  check_sum;
	unsigned char  check_mod;	
}to_tty_date_oport_st;

//cmd_code C4
typedef struct{
	unsigned char  fix_lcode;
	unsigned char  fix_hcode;//fixed mode 2byte

	unsigned char  cmd_code;//C4
	unsigned char  op_code;  //60
	unsigned char  year;     //year
	unsigned char  month;    //month
	unsigned char  day;      //day
	unsigned char  hour;     //hour
	unsigned char  min;      //minute  
	unsigned char  sec;		 //second
	unsigned char  check_sum;
	unsigned char  check_mod;		
}to_tty_time_oport_st;

typedef struct{
	unsigned char h_data;
	unsigned char l_data;
}short_to_char;
//上行数据
//reply param
typedef struct{
	unsigned char  fix_hcode;//fixed mode 2byte
	unsigned char  fix_lcode;

	unsigned char  op_code;  //
	unsigned char  tds;
	unsigned char  curTemp;
	unsigned char  status;
	short_to_char  curF;
	short_to_char  totalF;
	short_to_char  retainF;
	short_to_char  retainDay;//flow mode 0xFFFF
	short_to_char  level[MAX_LEVEL_NUM];	
	unsigned char  check_sum;
	unsigned char  check_mod;
}from_tty_respond_st;

typedef struct{
	unsigned char  fix_hcode;//fixed mode 2byte
	unsigned char  fix_lcode;

	unsigned char  op_code;  //
	
	unsigned char  water_state;//0 unuse water,1 userwater
    unsigned char  waterTemp;
	unsigned char  childLock;
	unsigned char  check_sum;
	unsigned char  check_mod;	
}from_tty_water_info;

typedef struct{
	unsigned char  fix_lcode;
	unsigned char  fix_hcode;//fixed mode 2byte

	unsigned char  op_code;  //
	unsigned char  state_code;
	unsigned char  check_sum;
	unsigned char  check_mod;	
}from_tty_status;



//java local data
typedef struct{
	jfieldID tds_id;	
	jfieldID temp_id;
	jfieldID state_id;
	jfieldID waterUsed_id;
	jfieldID waterSum_id;
	jfieldID waterSurplus_id;
	jfieldID timeSurplus_id;
	jfieldID Filter_id0;
	jfieldID Filter_id1;
	jfieldID Filter_id2;
	jfieldID Filter_id3;
	jfieldID Filter_id4;
}WATER_BASE_DATA_ST;

typedef struct{
	jfieldID useState_id;
	jfieldID temp_id;
	jfieldID childLock_id;
}WATER_INFO_DATA_ST;
#define HW_SERIAL_PORT "/dev/ttyS1"
static int s_fd = -1;
static int s_init_FieldId = 0;

static WATER_BASE_DATA_ST s_waterData = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
static WATER_INFO_DATA_ST s_waterInfo = {0, 0, 0};

#define WATER_DATA_CLASS "Lcom/kxw/smarthome/entity/BaseData;"
#define WATER_DATA_DESCR "mBaseData"
#define WATER_INFO_CLASS "Lcom/kxw/smarthome/entity/WaterStateInfo;"
#define WATER_INFO_DESCR "mWaterStateInfo"
#define MAX_NAME_LEN 16
#define ARR_NUM 12
const char fieldName[][MAX_NAME_LEN] = {"tds", 
							  "temperature",
							  "state",
							  "waterUsed",
							  "waterSum",
							  "waterSurplus",
							  "timeSurplus",
							  "firstFilter",
							  "secondFilter",
							  "thirdFilter",
							  "fourthFilter",
							  "fivethFilter"};
const char infoName[][MAX_NAME_LEN] = {"useState",
	                                   "temperature",
	                                   "childrenLock"};
/************************************************************************************
 * tty_open_port() open	the	tty	port
 *如果打开的是一个终端设备，这个程序不会成为对应这个端口的控制终端，
 *如果没有该标志，任何一个输入，例如键盘中止信号等，都将影响进程。
 ************************************************************************************/
 
int	 tty_open_port(const char	*dev_name)
{	
	int	fd = -1; /*	File descriptor	for	the	port */

	LOGI("--[S] tty_open_port, dev_name is [%s]", dev_name);
	fd = open(dev_name,	O_RDWR|O_NOCTTY|O_NDELAY);
	LOGI("--instance_fd is [%d]", fd);
	if (-1 == fd)
	{
		LOGE("--fd less zero, port open failure");
		return -1;
	}
	s_fd = fd;
	LOGI("--[E] tty_open_port");
	return (s_fd);
}
/*****************************************************
***close port
***
***
*****************************************************/
int tty_close_port(void)
{
	int ret = 0;
	LOGI("--[S]tty_close_port");
	if (s_fd >= 0 )
	{
		ret = close(s_fd);
	}
	LOGI("--[E]tty_close_port");	
	return ret;
}
/************************************************************************************
 * tty_set_port()	set	the	attributes of	the	tty
 ************************************************************************************/
 int tty_set_port(int fd, int nSpeed, int nBits, int nStop)
{
	struct	termios	new_ios,old_ios;

	if (0 != tcgetattr(fd, &new_ios))
		NULL;

	LOGI("--[S]tty_set_port");
	bzero( &old_ios	,sizeof(struct termios));
	old_ios=new_ios;

	cfmakeraw(&new_ios);
	/*
 	* Enable the receiver and set local mode...
 	*/
	new_ios.c_cflag	|= CLOCAL|CREAD ;//设置控制模式状态，本地连接，接收使能 
	new_ios.c_cflag &= ~CSIZE;//字符长度，设置数据位之前一定要屏掉这个位
	switch (nBits)
	{
	case 5:
		new_ios.c_cflag	|=CS5;
		break;
	case 6:
		new_ios.c_cflag	|=CS6;
		break;
	case 7:
		new_ios.c_cflag	|=CS7;
		break;
	case 8:
		new_ios.c_cflag	|=CS8;
		break;
	default:
		break;
	}
	
	switch (nSpeed )
	{
	case 2400:
		cfsetispeed(&new_ios, B2400);
		cfsetospeed(&new_ios, B2400);
		break;
	case 4800:
		cfsetispeed(&new_ios, B4800);
		cfsetospeed(&new_ios, B4800);
		break;
	case 9600:
		cfsetispeed(&new_ios, B9600);
		cfsetospeed(&new_ios, B9600);
		break;
	case 19200:
		cfsetispeed(&new_ios, B19200);
		cfsetospeed(&new_ios, B19200);
		break;
	case 115200:
		cfsetispeed(&new_ios, B115200);
		cfsetospeed(&new_ios, B115200);
		break;
	case 460800:
		cfsetispeed(&new_ios, B460800);
		cfsetospeed(&new_ios, B460800);
		break;
	default:
		break;
	}
	if (1 == nStop)
		new_ios.c_cflag	&= ~CSTOPB;
	else
		new_ios.c_cflag	|= CSTOPB;
 
	tcflush(fd,TCIOFLUSH);
	if ( 0 != tcsetattr(fd,TCSANOW,&new_ios))//TCSANOW标志所有改变必须立刻生效而不用等到数据传输结束
	{
		tcsetattr(fd,TCSANOW,&old_ios);
		LOGI("--[M]tty_set_port");
		return -1;
	}
	LOGI("--[E]tty_set_port");
	return	0;
}

/************************************************
**int fd：文件描述符(设备描述符),unsigned char cmd 命令字
**ret 成功返回写入数据的字节数,该值通常等于len，
**    如果写入失败返回-1
************************************************/
int sent_com_info_port(int fd, unsigned char cmd,unsigned char op, unsigned short value)
{
	int ret = -1;
	unsigned short sum = 0;
	unsigned char h_value = 0;
	unsigned char l_value = 0;
	to_tty_com_oport_st data;

	LOGI("--[S] sent_com_info_port: cmd is [%#02X], op is [%#02X]", cmd, op);
	memset((void *)&data, 0,sizeof(data));

	h_value = (unsigned char)((value&0xFF00)>>8);
	l_value = (unsigned char)(value&0xFF);
	
	LOGI("--value is [%d], 16 value is [%#02X], [%#02X]", value, h_value, l_value);

	data.fix_lcode = 0x96;
	data.fix_hcode = 0xF0;
	
	data.cmd_code = cmd;
	data.op_code = op;
	data.h_data = h_value;
	data.l_data = l_value;
	LOGI("--h_data and l_data is [%#02X%02X]", data.h_data, data.l_data);
	
	sum = (unsigned short) (data.fix_lcode + data.fix_hcode + data.cmd_code + data.op_code + data.l_data + data.h_data);
	data.check_sum = (unsigned char)((sum&0xFF00)>>8);
	data.check_mod = (unsigned char)(sum&0x00FF);
	LOGI("--verify value check_sum and check_mod is [%#02X%02X]", data.check_sum, data.check_mod);


    LOGI("--data is [%02X%02X%02X%02X%02X%02X%02X%02X]", data.fix_lcode, data.fix_hcode, data.cmd_code, data.op_code, data.h_data, data.l_data, data.check_sum, data.check_mod);
	ret = write(fd, (unsigned char *)&data,sizeof(to_tty_com_oport_st));
	LOGI("--sent return value is [%d],sent len is [%d]", ret, sizeof(data));

	LOGI("--[E] sent_com_info_port");
	return ret;
}

int sent_filter_info(int fd, unsigned char cmd, unsigned short value[], int len)
{
	int ret = -1;
	int delay = 10;
	int loop = 0;
	unsigned short sum = 0;
	unsigned char check_sum = 0;
	unsigned char check_mod = 0;
	unsigned char _data[15] = {0x96, 0xF0, cmd, 
		                      (unsigned char)(0xFF&(value[0]>>8)), (unsigned char)(0xFF&value[0]), 
		                      (unsigned char)(0xFF&(value[1]>>8)), (unsigned char)(0xFF&value[1]),
		                      (unsigned char)(0xFF&(value[2]>>8)), (unsigned char)(0xFF&value[2]),
                              (unsigned char)(0xFF&(value[3]>>8)), (unsigned char)(0xFF&value[3]),
							  (unsigned char)(0xFF&(value[4]>>8)), (unsigned char)(0xFF&value[4]),
							  0,0};	
	LOGI("--[S] sent_filter_info: cmd is [0x96F0%02X], data is [%#02X%#02X%#02X%#02X%#02X]", cmd, value[0], value[1], value[2], value[3], value[4]);

	for(int i = 0; loop < 13; loop++)
	{
		sum += (unsigned short)_data[loop];
	}
	check_sum = (unsigned char)((sum&0xFF00)>>8);
	check_mod = (unsigned char)(sum&0x00FF);
	LOGI("--[M] verify value check_sum and check_mod is [%#02X%02X]", check_sum, check_mod);	
	_data[13] = check_sum;
	_data[14] = check_mod;
	do{
		ret = write(fd, _data,sizeof(_data));
		if (ret < 0)
		{
			LOGE("--[M] fail to sent data, delay [%d]ms try again time [%d]",delay,loop);
			loop++;
			usleep(delay*1000);
		}
	}while(ret < 0 && loop < 10);
	LOGI("--[M] sent return value is [%d],sent len is [%d]", ret, sizeof(_data));
	
	LOGI("--[E] sent_filter_info");

	return ret;	
}


int sent_com2_info_port(int fd, unsigned char cmd,unsigned char data_0, unsigned char data_1, unsigned char data_2)
{
	int ret = -1;
	int loop = 0;
	int delay = 10;
	unsigned short sum = 0;
	to_tty_com_oport2_st data;

	LOGI("--[S] sent_com_info_port: cmd is [%#02X], data is [%#02X%#02X%#02X]", cmd, data_0, data_1, data_2);
	memset((void *)&data, 0,sizeof(data));
	data.fix_hcode = 0x96;
	data.fix_lcode = 0xF0;
	data.cmd_code = cmd;
	data.data_0 = data_0;
	data.data_1 = data_1;
	data.data_2 = data_2;

	sum = (unsigned short)(data.fix_hcode + data.fix_lcode + data.cmd_code + data.data_0 + data.data_1 + data.data_2);
	data.check_sum = (unsigned char)((sum&0xFF00)>>8);
	data.check_mod = (unsigned char)(sum&0x00FF);
	LOGI("--verify value check_sum and check_mod is [%#02X%02X]", data.check_sum, data.check_mod);
	LOGI("--data is [%02X%02X%02X%02X%02X%02X%02X%02X]", data.fix_hcode, data.fix_lcode, data.cmd_code, data.data_0, data.data_1, data.data_2, data.check_sum, data.check_mod);
	do{
		ret = write(fd, (unsigned char *)&data,sizeof(to_tty_com_oport2_st));
		if (ret < 0)
		{
			LOGE("--fail to sent data, delay [%d]ms try again time [%d]",delay,loop);
			loop++;
			usleep(delay*1000);
		}
	}while(ret < 0 && loop < 10);

	LOGI("--sent return value is [%d],sent len is [%d]", ret, sizeof(data));
	
	LOGI("--[E] sent_com_info_port");
	
	return ret;	
}


int sent_special_info_port(int fd, unsigned char cmd,unsigned char op, unsigned char flag, unsigned short value)
{
	int ret = -1;
	unsigned short sum = 0;
	unsigned char h_value = 0;
	unsigned char l_value = 0;
	to_tty_special_oport_st data;

	LOGI("--[S] sent_special_info_port: cmd is [%#02X], op is [%#02X],flag is [%#02X]", cmd, op, flag);
	memset((void *)&data, 0,sizeof(data));

	h_value = (unsigned char)((value&0xFF00)>>8);
	l_value = (unsigned char)(value&0xFF);
	
	LOGI("--value is [%d], 16 mode is [%#02X], [%#02X]", value, h_value, l_value);

	data.fix_lcode = 0x96;
	data.fix_hcode = 0xF0;
	
	data.cmd_code = cmd;
	data.op_code = op;
	data.flag_code = flag;
	data.h_data = h_value;
	data.l_data = l_value;
	LOGI("--h_data and l_data is [%#02X%02X]", data.h_data, data.l_data);
	
	sum = (unsigned short) (data.fix_lcode + data.fix_hcode + data.cmd_code + data.op_code + data.flag_code + data.l_data + data.h_data);
	data.check_sum = (unsigned char)((sum&0xFF00)>>8);
	data.check_mod = (unsigned char)(sum&0x00FF);
	LOGI("--verify value check_sum and check_mod is [%#02X%02X]", data.check_sum, data.check_mod);


    LOGI("--data is [%02X%02X%02X%02X%02X%02X%02X%02X%02X]", data.fix_lcode, data.fix_hcode, data.cmd_code, data.op_code, data.flag_code, data.h_data, data.l_data, data.check_sum, data.check_mod);
	ret = write(fd, (unsigned char *)&data,sizeof(to_tty_special_oport_st));
	LOGI("--sent return value is [%d],sent len is [%d]", ret, sizeof(data));
	LOGI("--[E] sent_special_info_port");
	return ret;
}

/*****************************************************
***取机器参数信息
***
***
*****************************************************/
int set_machine_param(void)
{
	LOGI("--get machine param");
	return sent_com2_info_port(s_fd, 0xC0, 0x00, 0x00, 0x00);
}

/*****************************************************
***设置计费模式
***0x00按流量0x01按天数
***
*****************************************************/
int set_charge_mode(unsigned char flag)
{
	LOGI("--set charge mode is [%#02X]", flag);
	return sent_com2_info_port(s_fd, 0xC1, flag, 0x00, 0x00);	
}


/*****************************************************
***设置流量
***
***
*****************************************************/
int set_flow_value(unsigned short value)
{
	unsigned char data_0 = (unsigned char)((value>>8)&0xFF);
	unsigned char data_1 = (unsigned char)(value&0xFF);
	LOGI("--set flow value is [%d], hight and low value is [%02X%02X]", value,data_0, data_1);
	return sent_com2_info_port(s_fd, 0xC2,data_0, data_1,0x00);
}

/*****************************************************
***设置天数
***
***
*****************************************************/
int set_day_value(unsigned short value)
{
	unsigned char data_0 = (unsigned char)((value&0xFF00)>>8);
	unsigned char data_1 = (unsigned char)(value&0xFF);
	LOGI("--set day is [%d], hight and low value is [%02X%02X]", value,data_0, data_1);
	return sent_com2_info_port(s_fd, 0xC3,data_0, data_1,0x00);

}


/*****************************************************
***用水与关水
***
***
*****************************************************/
int set_open_water(unsigned short temp)
{
	unsigned char data_0 = (unsigned char)(temp&0xFF);
	LOGI("--set open water tempreture is [%d,%#02X]", temp, data_0);
	return sent_com2_info_port(s_fd, 0xC4, data_0, 0x00, 0x00);		
}

int set_close_water(void)
{
	LOGI("--set close water");
	return sent_com2_info_port(s_fd, 0xC5, 0xFF, 0xFF, 0xFF);

}


/*****************************************************
***set filter life
***
***
*****************************************************/
int set_filter_life(unsigned char level, unsigned short value)
{
	unsigned char data_0 = (unsigned char)((value&0xFF00)>>8);
	unsigned char data_1 = (unsigned char)(value&0xFF);
	LOGI("--set filter life :level is [%d], hour is [%d], hight and low is [%#02X, %#02X]", level, value, data_0, data_1);
	return sent_com2_info_port(s_fd, 0xC6, level,data_0,data_1);
}


/*****************************************************
***set ver switch 0x00 close , 0x01 open
***
***
*****************************************************/

int set_version_shift(unsigned char shift)
{
	LOGI("--set version shift:0x00 close, 0x01 open");	
	return sent_com2_info_port(s_fd, 0xC7, shift, 0x00, 0x00);
}

/*****************************************************
***clear bind data
***
***
*****************************************************/
int clear_bind_data()
{
	LOGI("--clear bind data");
	return sent_com2_info_port(s_fd,0xCA,0x01,0x00,0x00);
}


/*****************************************************
***校准time
***
***
*****************************************************/
int sent_time_info_port(int fd, unsigned char y,unsigned char m, unsigned char d,unsigned char h, unsigned char min, unsigned char s)
{
	int ret = -1;
	unsigned short sum = 0;
	to_tty_time_oport_st data;
	LOGI("--[S] sent_time_info_port: time is [%02X%02X%02X%02X%02X%02X]\n", y, m, d, h,min,s);
	memset((void *)&data, 0,sizeof(data));

	data.fix_lcode = 0x96;
	data.fix_hcode = 0xF0;

	data.cmd_code = 0xC8;
	data.op_code = 0xAA;
	data.year = y;
	data.month = m;
	data.day = d;
	data.hour = h;
	data.min = min;
	data.sec = s;

	sum =(unsigned short) (data.fix_lcode + data.fix_hcode +data.cmd_code + data.op_code + data.year + data.month + data.day + data.hour + data.min + data.sec);
	data.check_sum = (unsigned char)((sum&0xFF00)>>8);
	data.check_mod = (unsigned char)(sum&0x00FF);
	LOGI("--verify value check_sum and check_mod is [%#02X%02X]", data.check_sum, data.check_mod);

	LOGI("--data is [%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X]", data.fix_lcode, data.fix_hcode,data.cmd_code, data.op_code, data.year, data.month, data.day, 
		                                        data.hour,data.min, data.sec, data.check_sum, data.check_mod);
	
	ret = write(fd, (unsigned char *)&data,sizeof(to_tty_time_oport_st));
	LOGI("--sent return value is [%d],sent len is [%d]", ret, sizeof(data));
	LOGI("--[E] sent_time_info_port");
	return ret;	
}


/************************************************
**int fd：文件描述符(设备描述符)
**char *pBuffer：数据缓冲区
**int len：要读取的字节数

**struct termios{
**    tcflag_t    c_iflag;   //input flag
**    tcflag_t    c_oflag;  //output flag
**    tcflag_t    c_cflag;  //control flag
**    tcflag_t    c_lflag;   //local flag
**    cc_t        c_cc[NCCS];  //control  characters
**}
**ret 读操作成功读取返回读取的字节数，失败则返回-1
************************************************/
int recv_info_port(int fd, unsigned char *pBuffer, int len)
{
	int ret = -1;
	//int oldFlag = 0;
	//int newFlag = 0;
    int loop = 0;
	int delay = 10;
	LOGI("--[S] recv_info_port");
	//fcntl(uartfd, F_SETFL, 0); //阻塞
	//fcntl(fd, F_SETFL, FNDELAY); //非阻塞
	//newFlag = oldFlag = fcntl(fd,F_GETFL);//先获取原文件状态(假定函数执行成功返回文件状态)
	//LOGI("--[M] set noblock");
	//fcntl(fd, F_SETFL, newFlag);
	memset((void *)pBuffer,0, len);
	LOGI("--[M] start read data");	
	do{
		ret = read(fd, pBuffer,len);
#if 1 == __KXW_TRACE_MODE__
		if (ret >= 0)
		{
			int loop = 0;
			do{
				LOGI("--recv data byte[%d]:[%X]",loop,pBuffer[loop]);
			}while(++loop < ret);
		}
#endif	
		if (ret < 0)
		{
			LOGE("--fail to recv data, delay [%d]ms try again time [%d]",delay,loop);
			loop++;
			usleep(delay*1000);
		}
	}while(ret < 0 && loop < 10);

	//fcntl(fd, F_SETFL, oldFlag);//设置为原始数据模式(Raw Date Mode)传输数据
	LOGI("--[E] recv_info_port");
	return ret;
}

int recv_status_port(void)
{
	int ret = -1;
	unsigned char buffer[MAX_BUFFER_SIZE+1];
	unsigned char value = 0;
	unsigned char op = 0;
	from_tty_status *pStatus = 0;
	unsigned char check_sum = 0;
	unsigned char check_mod = 0;
		
	LOGI("--[S] recv_status_port");
	ret = recv_info_port(s_fd, buffer, MAX_BUFFER_SIZE);
	LOGI("--return value is [%d]", ret);
	
	if (ret < 0 || ret > MAX_BUFFER_SIZE)
	{
		LOGE("--data read failure, ret is [%d]", ret);
		return -1;
	}
	if ((buffer[0]&0x9C) && (buffer[1]&0xF5))
	{
		op = buffer[2];
		LOGI("--[M] op value is [%#02X]", op);
		if (E_OP_9 == op || E_OP_A == op)
		{
			pStatus = (from_tty_status *)&buffer[0];
			check_sum = (unsigned char)(((pStatus->fix_lcode + pStatus->fix_hcode + pStatus->op_code + pStatus->state_code)&0xFF00)>>8);
			check_mod = (unsigned char)((pStatus->fix_lcode + pStatus->fix_hcode + pStatus->op_code + pStatus->state_code)&0x00FF);
			LOGI("--[M]verify value check_sum and check_mod is [%02X%02X]", check_sum, check_mod);				
			LOGI("--[M]recv data is [%02X%02X%02X%02X%02X%02X]", pStatus->fix_lcode, pStatus->fix_hcode, pStatus->op_code, pStatus->state_code, pStatus->check_sum, pStatus->check_mod);
			if ((check_sum != (check_sum & pStatus->check_sum)) || (check_mod != (check_mod & pStatus->check_mod)))
			{
				pStatus = 0x0000;
				LOGE("--data verify error");
			}
		}		
	}

	if (0x0000 == pStatus)
	{
		LOGE("--[M] cmd code or op code error");
		return -2;
	}
	LOGI("--[E] recv_status_port [%d]", (int)(0xFF&pStatus->state_code));
	return (int)(0xFF&pStatus->state_code);
	
}

int recv_machine_param(from_tty_respond_st **_pRespond)
{	
	int ret = -1;
	from_tty_respond_st *pRespond = 0;
	unsigned char buffer[MAX_BUFFER_SIZE+1] = {0};
	unsigned char op = 0;
	unsigned char state = 0;
	unsigned char check_sum = 0;
	unsigned char check_mod = 0;

	LOGI("--[S] recv_machine_param");
	ret = recv_info_port(s_fd, buffer, MAX_BUFFER_SIZE);
	LOGI("--[M] return value is [%d]", ret);	
	if (ret < 0 || ret > MAX_BUFFER_SIZE)
	{
		LOGE("--[M] data read failure, ret is [%d]", ret);
		return -1;
	}
	if ((buffer[0]&0x9C) && (buffer[1]&0xF5))
	{
		op = buffer[2];
		LOGI("--[M] op value is [%#02X]", op);
		if (E_OP_8 == op)
		{
			LOGI("--[M] normal open water");
			pRespond = (from_tty_respond_st *)&buffer[0];
			state = pRespond->status;		
			LOGI("--[M] current water tank status is [%#02X]",state);
#if 1 == __KXW_TRACE_MODE__
			switch(state){
				case 0x00:
					LOGI("--normal status");
					break;
				case 0x01:
					LOGI("--lack water status");
					break;
				case 0x02:
					LOGI("--water full status");
					break;
				case 0x04:
					LOGI("--maintain status");
					break;
				case 0x08:
					LOGI("--washing status");
					break;
				default:
					LOGE("--status value other");
					break;
			}
#endif			
			check_sum = (unsigned char)(((pRespond->fix_hcode + pRespond->fix_lcode + pRespond->op_code + pRespond->tds + pRespond->curTemp + pRespond->status + pRespond->curF.h_data + pRespond->curF.l_data +
			            pRespond->totalF.h_data + pRespond->totalF.l_data + pRespond->retainF.h_data + pRespond->retainF.l_data + pRespond->retainDay.h_data + pRespond->retainDay.l_data +
			            pRespond->level[0].h_data + pRespond->level[0].l_data + pRespond->level[1].h_data + pRespond->level[1].l_data + pRespond->level[2].h_data + pRespond->level[2].l_data +
			            pRespond->level[3].h_data + pRespond->level[3].l_data + pRespond->level[4].h_data + pRespond->level[4].l_data)&0xFF00)>>8);
			check_mod = (unsigned char)((pRespond->fix_hcode + pRespond->fix_lcode + pRespond->op_code + pRespond->tds + pRespond->curTemp + pRespond->status + pRespond->curF.h_data + pRespond->curF.l_data +
			            pRespond->totalF.h_data + pRespond->totalF.l_data + pRespond->retainF.h_data + pRespond->retainF.l_data + pRespond->retainDay.h_data + pRespond->retainDay.l_data +
			            pRespond->level[0].h_data + pRespond->level[0].l_data + pRespond->level[1].h_data + pRespond->level[1].l_data + pRespond->level[2].h_data + pRespond->level[2].l_data +
			            pRespond->level[3].h_data + pRespond->level[3].l_data + pRespond->level[4].h_data + pRespond->level[4].l_data)&0x00FF);
			LOGI("--[M] check value is [sum:%02X, mod:%02X], recv verify value is [sum:%02X, mod:%02X]",check_sum, check_mod, pRespond->check_sum, pRespond->check_mod);

			LOGI("--[M] respond data is [%#02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X]", 
				        pRespond->fix_hcode , pRespond->fix_lcode , pRespond->op_code , pRespond->tds , pRespond->curTemp , pRespond->status , pRespond->curF.h_data , pRespond->curF.l_data ,
			            pRespond->totalF.h_data , pRespond->totalF.l_data , pRespond->retainF.h_data , pRespond->retainF.l_data , pRespond->retainDay.h_data , pRespond->retainDay.l_data ,
			            pRespond->level[0].h_data , pRespond->level[0].l_data , pRespond->level[1].h_data , pRespond->level[1].l_data , pRespond->level[2].h_data , pRespond->level[2].l_data ,
			            pRespond->level[3].h_data , pRespond->level[3].l_data , pRespond->level[4].h_data , pRespond->level[4].l_data,pRespond->check_sum, pRespond->check_mod);			
			if ((check_sum != (check_sum & pRespond->check_sum)) || (check_mod != (check_mod & pRespond->check_mod)))
			{
				pRespond = 0;
				LOGE("--[M] data verify error");
			}	
		}	
	}
	*_pRespond = pRespond;
	if (pRespond)
		ret = (int)(state&0xFF);
	LOGI("--[E] recv_machine_param");
	return ret;
}

int recv_water_info(from_tty_water_info **pWater_Info)
{
	int ret = -1;
	unsigned char buffer[MAX_BUFFER_SIZE+1];
	unsigned char op = 0;
	from_tty_water_info *pWaterInfo = 0;
	unsigned char check_sum = 0;
	unsigned char check_mod = 0;
		
	LOGI("--[S] recv_status_port");
	ret = recv_info_port(s_fd, buffer, MAX_BUFFER_SIZE);
	LOGI("--return value is [%d]", ret);
	
	if (ret < 0 || ret > MAX_BUFFER_SIZE)
	{
		LOGE("--data read failure, ret is [%d]", ret);
		return -1;
	}
	if ((buffer[0]&0x9C) && (buffer[1]&0xF5))
	{
		op = buffer[2];
		LOGI("--[M] op value is [%#02X]", op);
		if (E_OP_B == op)
		{
			pWaterInfo = (from_tty_water_info *)&buffer[0];
			check_sum = (unsigned char)(((pWaterInfo->fix_lcode + pWaterInfo->fix_hcode + pWaterInfo->op_code + 
				                          pWaterInfo->water_state + pWaterInfo->waterTemp + pWaterInfo->childLock)&0xFF00)>>8);
			check_mod = (unsigned char)((pWaterInfo->fix_lcode + pWaterInfo->fix_hcode + pWaterInfo->op_code + 
				                          pWaterInfo->water_state + pWaterInfo->waterTemp + pWaterInfo->childLock)&0x00FF);
			LOGI("--[M]verify value check_sum and check_mod is [%02X%02X]", check_sum, check_mod);				
			LOGI("--[M]recv data is [%02X%02X%02X%02X%02X%02X%02X%02X]", pWaterInfo->fix_lcode, pWaterInfo->fix_hcode, pWaterInfo->op_code, 
				                                                 pWaterInfo->water_state, pWaterInfo->waterTemp , pWaterInfo->childLock, 
				                                                 pWaterInfo->check_sum, pWaterInfo->check_mod);
			if ((check_sum != (check_sum & pWaterInfo->check_sum)) || (check_mod != (check_mod & pWaterInfo->check_mod)))
			{
				pWaterInfo = 0x0000;
				LOGE("--data verify error");
			}
		}		
	}


	*pWater_Info = pWaterInfo;
	if (pWaterInfo)
		ret = (int)(pWaterInfo->water_state&0xFF);
	return ret;
}



JNIEXPORT jobject JNICALL Java_android_1serialport_1api_SerialPort_getFileDescriptor
	(JNIEnv *pEnv, jobject thiz)
{
	/* Create a corresponding file descriptor */
	jclass cFileDescriptor = NULL;
	jmethodID iFileDescriptor = NULL;
	jfieldID descriptorID = NULL;
	jboolean state = 0;
	jobject fileDescriptor = NULL;
	do{
		cFileDescriptor = pEnv->FindClass("java/io/FileDescriptor");
		if(pEnv->ExceptionOccurred()) 
		{
   			state = 1;
			break;
		}
		iFileDescriptor = pEnv->GetMethodID(cFileDescriptor, "<init>", "()V");
		if(pEnv->ExceptionOccurred()) 
		{
   			state = 1;
			break;
		}			
		descriptorID = pEnv->GetFieldID(cFileDescriptor, "descriptor", "I");
		if(pEnv->ExceptionOccurred()) 
		{
   			state = 1;
			break;
		}			
		fileDescriptor = pEnv->NewObject(cFileDescriptor, iFileDescriptor);
		if(pEnv->ExceptionOccurred()) 
		{
   			state = 1;
			break;
		}
		pEnv->SetIntField(fileDescriptor, descriptorID, (jint)s_fd);
	}while(0);


	return fileDescriptor;	

}

int init_water_base_data(JNIEnv *pEnv, jobject thiz, jobject thizData)
{
	int ret = 0;
	do{
		jclass serialPortClass =  pEnv->GetObjectClass(thiz);
		jclass waterDataClass = pEnv->GetObjectClass(thizData);
		if(pEnv->ExceptionOccurred()) 
		{
			ret = -1;
			break;
		}			
		jfieldID wdFdId = pEnv->GetFieldID(serialPortClass,WATER_DATA_DESCR,WATER_DATA_CLASS);
		if(pEnv->ExceptionOccurred()) 
		{
			ret = -1;
			break;
		}		
		s_waterData.tds_id = pEnv->GetFieldID(waterDataClass,fieldName[0],"I");
		if(pEnv->ExceptionOccurred()) 
		{
			ret = -1;
			break;
		}		
		s_waterData.temp_id= pEnv->GetFieldID(waterDataClass,fieldName[1],"I");
		if(pEnv->ExceptionOccurred()) 
		{
			ret = -1;
			break;
		}		
		s_waterData.state_id = pEnv->GetFieldID(waterDataClass,fieldName[2],"I");
		if(pEnv->ExceptionOccurred()) 
		{
			ret = -1;
			break;
		}		
		s_waterData.waterUsed_id = pEnv->GetFieldID(waterDataClass,fieldName[3],"I");
		if(pEnv->ExceptionOccurred()) 
		{
			ret = -1;
			break;
		}
		s_waterData.waterSum_id = pEnv->GetFieldID(waterDataClass,fieldName[4],"I");
		if(pEnv->ExceptionOccurred()) 
		{
			ret = -1;
			break;
		}		
		s_waterData.waterSurplus_id = pEnv->GetFieldID(waterDataClass,fieldName[5],"I");
		if(pEnv->ExceptionOccurred()) 
		{
			ret = -1;
			break;
		}		
		s_waterData.timeSurplus_id = pEnv->GetFieldID(waterDataClass,fieldName[6],"I");
		if(pEnv->ExceptionOccurred()) 
		{
			ret = -1;
			break;
		}		
		s_waterData.Filter_id0 = pEnv->GetFieldID(waterDataClass,fieldName[7],"I");
		if(pEnv->ExceptionOccurred()) 
		{
			ret = -1;
			break;
		}		
		s_waterData.Filter_id1 = pEnv->GetFieldID(waterDataClass,fieldName[8],"I");
		if(pEnv->ExceptionOccurred()) 
		{
			ret = -1;
			break;
		}		
		s_waterData.Filter_id2= pEnv->GetFieldID(waterDataClass,fieldName[9],"I");
		if(pEnv->ExceptionOccurred()) 
		{
			ret = -1;
			break;
		}		
		s_waterData.Filter_id3 = pEnv->GetFieldID(waterDataClass,fieldName[10],"I");
		if(pEnv->ExceptionOccurred()) 
		{
			ret = -1;
			break;
		}		
		s_waterData.Filter_id4= pEnv->GetFieldID(waterDataClass,fieldName[11],"I");
		if(pEnv->ExceptionOccurred()) 
		{
			ret = -1;
		}		
	}while(0);
	return ret;
}

int init_water_info_data(JNIEnv *pEnv, jobject thiz, jobject thizData)
{
	int ret = 0;
	do{
		jclass serialPortClass =  pEnv->GetObjectClass(thiz);
		jclass waterStateInfo = pEnv->GetObjectClass(thizData);

		jfieldID wdFdId = pEnv->GetFieldID(serialPortClass,WATER_INFO_DESCR,WATER_INFO_CLASS);
		if(pEnv->ExceptionOccurred()) 
		{
			ret = -1;
			break;
		}
		
		s_waterInfo.useState_id = pEnv->GetFieldID(waterStateInfo,infoName[0],"I");
		if(pEnv->ExceptionOccurred()) 
		{
			ret = -1;
			break;
		}

		s_waterInfo.temp_id = pEnv->GetFieldID(waterStateInfo,infoName[1],"I");
		if(pEnv->ExceptionOccurred()) 
		{
			ret = -1;
			break;
		}

		s_waterInfo.childLock_id = pEnv->GetFieldID(waterStateInfo,infoName[2],"I");
		if(pEnv->ExceptionOccurred()) 
		{
			ret = -1;
			break;
		}
				
	}while(0);
	return ret;
}



/*
 * Class:     android_serialport_api_SerialPort
 * Method:    open
 * Signature: (Ljava/lang/String;II)Ljava/io/FileDescriptor;
 */
JNIEXPORT jint JNICALL Java_android_1serialport_1api_SerialPort_open
  (JNIEnv *pEnv, jclass thiz, jstring pPath, jint baudrate, jint stop)
{
	jboolean iscopy;
	const char *path_utf = 0;
	int fd = s_fd;

	if (fd >= 0)
	{
		tty_close_port();
	}

	if (0 != pPath)
	{
		path_utf = pEnv->GetStringUTFChars(pPath, &iscopy);
		if(pEnv->ExceptionOccurred()) 
		{
   			return -1;
		}
		fd = tty_open_port(path_utf);
		pEnv->ReleaseStringUTFChars(pPath, path_utf);
	}
	else
	{
		fd = tty_open_port(HW_SERIAL_PORT);
	}
	if (fd < 0)
	{
		return -1;
	}	
	return tty_set_port(s_fd,baudrate,8,stop);	
}

/*
 * Class:     android_serialport_api_SerialPort
 * Method:    close
 * Signature: ()V
 * //方法的签名（类型签名1类型签名2...）返回值类型签名,类的答名后面有一个分号";"
 */
JNIEXPORT void JNICALL Java_android_1serialport_1api_SerialPort_close
  (JNIEnv *pEnv, jobject thiz)
{
#if 1 == __KXW_TRACE_MODE__
	jclass serialPortClass = pEnv->GetObjectClass(thiz);
	jclass fileDescriptorClass = pEnv->FindClass("java/io/FileDescriptor");
	jfieldID mFdID = pEnv->GetFieldID(serialPortClass, "mFd", "Ljava/io/FileDescriptor;");
	jfieldID descriptorID = pEnv->GetFieldID(fileDescriptorClass, "descriptor", "I");

	jobject mFd = pEnv->GetObjectField(thiz, mFdID);
	jint descriptor = pEnv->GetIntField(mFd, descriptorID);

	LOGI("--close(fd = %d), local fd is [%d]", descriptor, s_fd);	
#endif	
	tty_close_port();
	s_init_FieldId = 0;
}

JNIEXPORT jint JNICALL Java_android_1serialport_1api_SerialPort_initBaseData
  (JNIEnv *pEnv, jobject thiz, jobject thizData)
{
	int ret = 0;
	if (0 == s_init_FieldId)
	{
		ret = init_water_base_data(pEnv, thiz, thizData);
		if (1 == ret)
		{
			LOGE("--success to init water base data");
			s_init_FieldId = 1;
		}
	}
	return ret;
}



JNIEXPORT jint JNICALL Java_android_1serialport_1api_SerialPort_setBaseData
  (JNIEnv *pEnv, jobject thiz, jobject thisData)
{
	return sent_com2_info_port(s_fd, 0xC0, 0x00, 0x00, 0x00);   
}


JNIEXPORT jint JNICALL Java_android_1serialport_1api_SerialPort_getBaseData
  (JNIEnv *pEnv, jobject thiz, jobject thizData)
{
	from_tty_respond_st *pRespond = 0;
	if (recv_machine_param(&pRespond) >= 0 && pRespond)
	{
		do{
			pEnv->SetIntField(thizData,s_waterData.tds_id,(jint)pRespond->tds);
			pEnv->SetIntField(thizData,s_waterData.temp_id,(jint)pRespond->curTemp);
			pEnv->SetIntField(thizData,s_waterData.state_id, (jint)pRespond->status);
			pEnv->SetIntField(thizData,s_waterData.waterUsed_id,(jint)(pRespond->curF.l_data|((pRespond->curF.h_data<<8)&0xFF00)));
			pEnv->SetIntField(thizData,s_waterData.waterSum_id,(jint)(pRespond->totalF.l_data|((pRespond->totalF.h_data<<8)&0xFF00)));
			pEnv->SetIntField(thizData,s_waterData.waterSurplus_id,(jint)(pRespond->retainF.l_data|((pRespond->retainF.h_data<<8)&0xFF00)));
			pEnv->SetIntField(thizData,s_waterData.timeSurplus_id, (jint)(pRespond->retainDay.l_data|((pRespond->retainDay.h_data<<8)&0xFF00)));

			pEnv->SetIntField(thizData,s_waterData.Filter_id0, (jint)(pRespond->level[0].l_data|((pRespond->level[0].h_data<<8)&0xFF00)));
			pEnv->SetIntField(thizData,s_waterData.Filter_id1, (jint)(pRespond->level[1].l_data|((pRespond->level[1].h_data<<8)&0xFF00)));
			pEnv->SetIntField(thizData,s_waterData.Filter_id2, (jint)(pRespond->level[2].l_data|((pRespond->level[2].h_data<<8)&0xFF00)));
			pEnv->SetIntField(thizData,s_waterData.Filter_id3, (jint)(pRespond->level[3].l_data|((pRespond->level[3].h_data<<8)&0xFF00)));
			pEnv->SetIntField(thizData,s_waterData.Filter_id4, (jint)(pRespond->level[4].l_data|((pRespond->level[4].h_data<<8)&0xFF00)));
		}while(0);
		return 0;
	}
	return -1;	
}


JNIEXPORT jint JNICALL Java_android_1serialport_1api_SerialPort_getReturn
  (JNIEnv *pEnv, jobject thiz, jobject thizData)

{
	int ret = recv_status_port();
	return ret == 170?0:-1;
}



JNIEXPORT jint JNICALL Java_android_1serialport_1api_SerialPort_setPayType
  (JNIEnv *pEnv, jobject thiz, jint mode)
{
	jint ret = 0;
	unsigned char _mode = (unsigned char)(mode&0xFF);
	ret = set_charge_mode(_mode);
	return ret;
}

JNIEXPORT jint JNICALL Java_android_1serialport_1api_SerialPort_setWaterVolume
  (JNIEnv *pEnv, jobject thiz, jint flow)
{
	jint ret = 0;
	unsigned short _flow = (unsigned short)(flow&0xFFFF);
	ret = set_flow_value(_flow);
	return ret;	
}

JNIEXPORT jint JNICALL Java_android_1serialport_1api_SerialPort_setDueTime
  (JNIEnv *pEnv, jobject thiz, jint day)
{
	jint ret = 0;
	unsigned short _day = (unsigned short)(day&0xFFFF);
	ret = set_day_value(_day);
	return ret;	
}



JNIEXPORT jint JNICALL Java_android_1serialport_1api_SerialPort_setWaterSwitch
  (JNIEnv *pEnv, jobject thiz, jboolean flag, jint temp)
{
	if (1 == flag)
	{
		unsigned short _temp = (unsigned short)(temp&0xFFFF);
		return set_open_water(_temp);		
	}
	else
	{
		return set_close_water();
	}
}
JNIEXPORT jint JNICALL Java_android_1serialport_1api_SerialPort_setFilterLife
  (JNIEnv *pEnv, jobject thiz, jintArray value, jint num)
{
#define MAX_LEVEL_NUM 5
	unsigned char _level = (unsigned char)(num&0xFF);
    unsigned short _value[MAX_LEVEL_NUM] = {0};
	jint *pValue = pEnv->GetIntArrayElements(value, false); 
	for(int loop = 0; loop < num; loop++)
	{
		_value[loop] = (unsigned short)(pValue[loop]&0xFFFF);
	}
	return sent_filter_info(s_fd, 0xC6, _value, num);
	//return set_filter_life(_level,_value);
}


JNIEXPORT jint JNICALL Java_android_1serialport_1api_SerialPort_initWaterState
  (JNIEnv *pEnv, jobject thiz, jobject thizData)
{
	return init_water_info_data(pEnv,thiz,thizData);
}

JNIEXPORT jint JNICALL Java_android_1serialport_1api_SerialPort_getWaterState
  (JNIEnv *pEnv, jobject thiz, jobject thizData)
{
	from_tty_water_info *pWaterInfo = 0;

	if (recv_water_info(&pWaterInfo) >= 0 && pWaterInfo)
	{
		do{
			pEnv->SetIntField(thizData,s_waterInfo.useState_id,(jint)pWaterInfo->water_state);
			pEnv->SetIntField(thizData,s_waterInfo.temp_id,(jint)pWaterInfo->waterTemp);
			pEnv->SetIntField(thizData,s_waterInfo.childLock_id,(jint)pWaterInfo->childLock);
		}while(0);
		return 0;
	}
	return -1;
}

JNIEXPORT jint JNICALL Java_android_1serialport_1api_SerialPort_setWaterState
  (JNIEnv *pEnv, jobject thiz, jobject thizData)
{
	unsigned char userState = 0;
	unsigned char temperature = 0;
	unsigned char childLock_state = 0;
	do{
		userState = pEnv->GetIntField(thizData,s_waterInfo.useState_id);
		temperature = pEnv->GetIntField(thizData,s_waterInfo.temp_id);
		childLock_state = pEnv->GetIntField(thizData,s_waterInfo.childLock_id);			
	}while(0);
	return sent_com2_info_port(s_fd,0xC9,userState,temperature,childLock_state);
}


JNIEXPORT jint JNICALL Java_android_1serialport_1api_SerialPort_setUnbind
  (JNIEnv *pEnv, jobject thiz)
{
	return clear_bind_data();
}

JNIEXPORT jint JNICALL Java_android_1serialport_1api_SerialPort_setVerSwitch
  (JNIEnv *pEnv, jobject thiz, jboolean shift)
{
	return set_version_shift(shift);
}

JNIEXPORT jint JNICALL Java_android_1serialport_1api_SerialPort_getReturn
  (JNIEnv *pEnv, jobject thiz)
{
	return recv_status_port();
}

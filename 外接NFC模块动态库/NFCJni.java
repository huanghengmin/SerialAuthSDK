package com.lz.nfc.jni;

public class NFCJni {
	static{
		System.loadLibrary("NfcIdJni");
	}
	
	public static int ANDROID_OTG_DEV =   3;
	
	//��ȡ�豸���к�
	public native int GetDevSerialNo(byte[] var1, int len);
	//��ȡ�����
	public native int GetLastError();
	//�����豸����
	public native void SetNFCLocal(int type, int devfd);

	//����NFC������
	public native void SetNFCServer(String IP, int Port);
	//NFC����
	public native int NFCCardReader(byte[] msg);

}

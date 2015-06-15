package com.tt.engtrain;

/**
 * @author mo 智能考察 文件信息类
 */
public class FileListMode {
	private String name;
	private String resourceId;
	private String uploadTime;
	private String daytime;
	private int fileState;// 后加 1 文件不存在 2 文件正在下载 3 已经存在
	private boolean isLocal = true;
	private int progress;
	private String downloadText;

	public FileListMode() {
		super();
	}

	public FileListMode(String name, String resourceId, String uploadTime, String daytime, int fileState, boolean isLocal, int progress, String downloadText) {
		super();
		this.name = name;
		this.resourceId = resourceId;
		this.uploadTime = uploadTime;
		this.daytime = daytime;
		this.fileState = fileState;
		this.isLocal = isLocal;
		this.progress = progress;
		this.downloadText = downloadText;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getDaytime() {
		return daytime;
	}

	public void setDaytime(String daytime) {
		this.daytime = daytime;
	}

	public int getFileState() {
		return fileState;
	}

	public void setFileState(int fileState) {
		this.fileState = fileState;
	}

	public boolean isLocal() {
		return isLocal;
	}

	public void setLocal(boolean isLocal) {
		this.isLocal = isLocal;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public String getDownloadText() {
		return downloadText;
	}

	public void setDownloadText(String downloadText) {
		this.downloadText = downloadText;
	}

}

class Filee {
	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "Filee [path=" + path + "]";
	}

}

class Namevalues {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Namevalues [name=" + name + "]";
	}

}
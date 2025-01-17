package tools.refinery.language.web.xtext.server.message;

import java.util.Map;
import java.util.Objects;

import com.google.gson.annotations.SerializedName;

public class XtextWebRequest {
	private String id;

	@SerializedName("request")
	private Map<String, String> requestData;

	public XtextWebRequest(String id, Map<String, String> requestData) {
		super();
		this.id = id;
		this.requestData = requestData;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, String> getRequestData() {
		return requestData;
	}

	public void setRequestData(Map<String, String> requestData) {
		this.requestData = requestData;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, requestData);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		XtextWebRequest other = (XtextWebRequest) obj;
		return Objects.equals(id, other.id) && Objects.equals(requestData, other.requestData);
	}

	@Override
	public String toString() {
		return "XtextWebSocketRequest [id=" + id + ", requestData=" + requestData + "]";
	}
}

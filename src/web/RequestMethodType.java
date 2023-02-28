package web;

public enum RequestMethodType {
    GET,
    POST,
    DELETE,
    UNKNOWN;

    public static RequestMethodType from(String method){
        for (RequestMethodType methodType: RequestMethodType.values()) {
            if(method.equals(methodType.name()))
                return methodType;
        }
        return UNKNOWN;
    }
}

package cn.vko.core.web.loader;

import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.nutz.ioc.IocLoader;
import org.nutz.ioc.IocLoading;
import org.nutz.ioc.ObjectLoadException;
import org.nutz.ioc.loader.json.JsonLoader;
import org.nutz.ioc.meta.IocObject;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.resource.NutResource;
import org.nutz.resource.Scans;

import cn.vko.core.common.util.MapUtil;

public class EnvJsLoader implements IocLoader {
	private static final Log log = Logs.get();
	private static final String ENV_KEY = "env";
	private JsonLoader jsonLoader;
	private Map<String, String> envMap;

	public EnvJsLoader(String... paths) {
		envMap = MapUtil.map();
		try {
			List<NutResource> list = Scans.me().loadResource(
					"^(.+[.])(js|json)$", paths);
			for (NutResource nr : list)
				loadFromReader(nr.getReader());
		} catch (Exception e) {
			if (log.isDebugEnabled())
				log.debug(e);
		}
		if (envMap.size() > 1) {
			log.warn("please check env js,contains more than one bean");
		}
		if (log.isDebugEnabled())
			log.debugf("Loaded %d bean define from path=%s --> %s,",
					envMap.size(), Arrays.toString(paths), envMap.keySet());
		String envPath = "config/dev";
		if (envMap.containsKey(ENV_KEY)) {
			envPath = envMap.get(ENV_KEY);
		}
		if (log.isDebugEnabled())
			log.debugf("env path=%s", envPath);
		jsonLoader = new JsonLoader(envPath);
	}

	@SuppressWarnings("unchecked")
	private void loadFromReader(Reader reader) {
		String s = Lang.readAll(reader);
		Map<String, String> map = (Map<String, String>) Json.fromJson(s);
		if (null != map && map.size() > 0)
			envMap.putAll(map);
	}

	@Override
	public String[] getName() {
		return jsonLoader.getName();
	}

	@Override
	public IocObject load(IocLoading loading, String name)
			throws ObjectLoadException {
		return jsonLoader.load(loading, name);
	}

	@Override
	public boolean has(String name) {
		return jsonLoader.has(name);
	}

}

package com.sunsai.graduation.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthenticationFilter implements Filter {

	// TODO: NEED LOAD FROM CONFIG
	private static String[] s_anonymousUrls = { "/", "\\/Home"};
	private static String[] s_userOnlyUrls = { "\\/User\\/.*" };

	private static String[] s_managerOnlyUrls = { "\\/Manager\\/.*" };

	private static ArrayList<Pattern> s_anonymousPatterns = new ArrayList<Pattern>();
	private static ArrayList<Pattern> s_userPatterns = new ArrayList<Pattern>();
	private static ArrayList<Pattern> s_managerPatterns = new ArrayList<Pattern>();

	static {
		s_anonymousPatterns = new ArrayList<Pattern>();
		s_userPatterns = new ArrayList<Pattern>();
		s_managerPatterns = new ArrayList<Pattern>();
		for (String anonymousUrl : s_anonymousUrls) {

			s_anonymousPatterns.add(Pattern.compile(anonymousUrl));
		}
		for (String studentUrl : s_userOnlyUrls) {
			s_userPatterns.add(Pattern.compile(studentUrl));
		}

		for (String teacherUrl : s_managerOnlyUrls) {
			s_managerPatterns.add(Pattern.compile(teacherUrl));
		}

	}

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		String servletPath = ((HttpServletRequest) request).getServletPath();
		boolean authorized = false;

		authorized |= urlMathPatterns(servletPath, s_anonymousPatterns);

		if (!authorized) {
			((HttpServletResponse) response).sendRedirect(((HttpServletRequest) request).getContextPath() + "/");
			return;
		}
		chain.doFilter(request, response);
	}

	private static boolean urlMathPatterns(String url, ArrayList<Pattern> patterns) {
		for (Pattern pattern : patterns) {
			Matcher anonymousMather = pattern.matcher(url);
			if (anonymousMather.matches()) {
				return true;
			}
		}

		return false;
	}

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}

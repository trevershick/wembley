package com.railinc.r2dq.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.batch.item.adapter.AbstractMethodInvokingDelegator.InvocationTargetThrowableWrapper;
import org.springframework.batch.item.adapter.DynamicMethodInvocationException;
import org.springframework.batch.item.adapter.HippyMethodInvoker;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.util.Assert;
import org.springframework.util.MethodInvoker;

public class TargetMethodInvoker<T> implements InitializingBean {
	private Object targetObject;
	private String targetMethod;
	private Object[] arguments;
	private boolean returnOriginalMessage;

	public Message<?> invoke(Message<?> message) throws Exception {
		T t = invokeDelegateMethodWithArgument(message.getPayload());
		if (returnOriginalMessage && Void.TYPE.equals(getTargetMethodReturnType())) {
			return message;
		}
		return MessageBuilder.withPayload(t).copyHeaders(message.getHeaders())
				.build();
	}

	protected T invokeDelegateMethodWithArgument(Object object)
			throws Exception {
		MethodInvoker invoker = createMethodInvoker(targetObject, targetMethod);
		invoker.setArguments(new Object[] { object });
		return doInvoke(invoker);
	}

	/**
	 * Create a new configured instance of {@link MethodInvoker}.
	 */
	private MethodInvoker createMethodInvoker(Object targetObject,
			String targetMethod) {
		HippyMethodInvoker invoker = new HippyMethodInvoker();
		invoker.setTargetObject(targetObject);
		invoker.setTargetMethod(targetMethod);
		invoker.setArguments(arguments);
		return invoker;
	}

	/**
	 * Prepare and invoke the invoker, rethrow checked exceptions as unchecked.
	 * 
	 * @param invoker
	 *            configured invoker
	 * @return return value of the invoked method
	 */
	@SuppressWarnings("unchecked")
	private T doInvoke(MethodInvoker invoker) throws Exception {
		try {
			invoker.prepare();
		} catch (ClassNotFoundException e) {
			throw new DynamicMethodInvocationException(e);
		} catch (NoSuchMethodException e) {
			throw new DynamicMethodInvocationException(e);
		}

		try {
			return (T) invoker.invoke();
		} catch (InvocationTargetException e) {
			if (e.getCause() instanceof Exception) {
				throw (Exception) e.getCause();
			} else {
				throw new InvocationTargetThrowableWrapper(e.getCause());
			}
		} catch (IllegalAccessException e) {
			throw new DynamicMethodInvocationException(e);
		}
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(targetObject);
		Assert.hasLength(targetMethod);
		Assert.state(targetClassDeclaresTargetMethod(),
				"target class must declare a method with matching name and parameter types");
	}

	/**
	 * @return true if target class declares a method matching target method
	 *         name with given number of arguments of appropriate type.
	 */
	private boolean targetClassDeclaresTargetMethod() {
		MethodInvoker invoker = createMethodInvoker(targetObject, targetMethod);

		Method[] memberMethods = invoker.getTargetClass().getMethods();
		Method[] declaredMethods = invoker.getTargetClass()
				.getDeclaredMethods();

		List<Method> allMethods = new ArrayList<Method>();
		allMethods.addAll(Arrays.asList(memberMethods));
		allMethods.addAll(Arrays.asList(declaredMethods));

		String targetMethodName = invoker.getTargetMethod();

		for (Method method : allMethods) {
			if (method.getName().equals(targetMethodName)) {
				Class<?>[] params = method.getParameterTypes();
				if (arguments == null) {
					// don't check signature, assume arguments will be supplied
					// correctly at runtime
					return true;
				}
				if (arguments.length == params.length) {
					boolean argumentsMatchParameters = true;
					for (int j = 0; j < params.length; j++) {
						if (arguments[j] == null) {
							continue;
						}
						if (!(params[j].isAssignableFrom(arguments[j]
								.getClass()))) {
							argumentsMatchParameters = false;
						}
					}
					if (argumentsMatchParameters)
						return true;
				}
			}
		}

		return false;
	}

	private Type getTargetMethodReturnType() {
		MethodInvoker invoker = createMethodInvoker(targetObject, targetMethod);

		Method[] memberMethods = invoker.getTargetClass().getMethods();
		Method[] declaredMethods = invoker.getTargetClass()
				.getDeclaredMethods();

		List<Method> allMethods = new ArrayList<Method>();
		allMethods.addAll(Arrays.asList(memberMethods));
		allMethods.addAll(Arrays.asList(declaredMethods));

		String targetMethodName = invoker.getTargetMethod();

		for (Method method : allMethods) {
			if (method.getName().equals(targetMethodName)) {
				return method.getGenericReturnType();
			}
		}
		return null;
	}

	public Object getTargetObject() {
		return targetObject;
	}

	public void setTargetObject(Object targetObject) {
		this.targetObject = targetObject;
	}

	public String getTargetMethod() {
		return targetMethod;
	}

	public void setTargetMethod(String targetMethod) {
		this.targetMethod = targetMethod;
	}

	public Object[] getArguments() {
		return arguments;
	}

	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}

	public boolean isReturnOriginalMessage() {
		return returnOriginalMessage;
	}

	public void setReturnOriginalMessage(boolean returnOriginalMessage) {
		this.returnOriginalMessage = returnOriginalMessage;
	}

}

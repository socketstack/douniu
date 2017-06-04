package com.rxqp.common.enums;

public enum NNTypeEnum {
	// 错误类型
	NNT_ERROR(0),
	// 无牛
	NNT_NONE(1),
	// 牛一
	NNT_SPECIAL_NIU1(2),
	// 牛二
	NNT_SPECIAL_NIU2(3),
	// 牛三
	NNT_SPECIAL_NIU3(4),
	// 牛四
	NNT_SPECIAL_NIU4(5),
	// 牛五
	NNT_SPECIAL_NIU5(6),
	// 牛六
	NNT_SPECIAL_NIU6(7),
	// 牛七
	NNT_SPECIAL_NIU7(8),
	// 牛八
	NNT_SPECIAL_NIU8(9),
	// 牛九
	NNT_SPECIAL_NIU9(10),
	// 牛牛
	NNT_SPECIAL_NIUNIU(11),
	// 五花牛
	NNT_SPECIAL_NIUHUA(12),
	// 炸弹
	NNT_SPECIAL_BOMEBOME(13);

	private Integer value;

	private NNTypeEnum(int value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public static NNTypeEnum getNNTypeEnumByValue(Integer value) {
		for (NNTypeEnum pt : NNTypeEnum.values()) {
			if (pt.getValue().equals(value)) {
				return pt;
			}
		}
		return null;
	}
}

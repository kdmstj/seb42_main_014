import ChildCareIcon from "@mui/icons-material/ChildCare";
import ForestIcon from "@mui/icons-material/Forest";
import PetsIcon from "@mui/icons-material/Pets";
import ElderlyIcon from "@mui/icons-material/Elderly";
import AccessibleIcon from "@mui/icons-material/Accessible";
import SvgIcon from "@mui/material/SvgIcon";
import styled from "styled-components";
import { useState } from "react";

interface CategoryProps {
	onCategoryClick: (category: string) => void;
}

const Body = styled.div`
	width: 100%;
	height: 100%;
	display: flex;
	justify-content: center;
	margin: 20px;
`;
const Flex = styled.div`
	width: 70%;
	height: 100%;
	display: flex;
	justify-content: space-around;
`;
const Icon = styled.div`
	background-color: #ffffff;
	margin-bottom: 10px;
	font-size: 50px;
	text-align: center;
	border: 3px solid rgb(193, 193, 193);
	border-radius: 100%;
	width: 90px;
	height: 90px;
	line-height: 100px;
	cursor: pointer;

	:hover {
		background-color: #fffffa;
	}
`;
const Col = styled.div`
	cursor: pointer;
	display: flex;
	flex-direction: column;
	align-items: center;
	span {
		font-size: 20px;
		font-weight: bold;
	}
`;
const All = styled.div`
	cursor: pointer;
	border: 3px solid rgb(193, 193, 193);
	background-color: #ffffff;
	text-align: center;
	border-radius: 100%;
	width: 90px;
	height: 90px;
	line-height: 80px;
	font-weight: bold;
	display: flex;
	flex-direction: column;
	:hover {
		background-color: #fffffa;
	}
	span:first-child {
		font-size: 30px;
		font-weight: bold;
	}
	span:last-child {
		line-height: 65px;
	}
`;

export default function Category({ onCategoryClick }: CategoryProps) {
	const handleClick = (category: string) => {
		onCategoryClick(category);
	};
	return (
		<Body>
			<Flex>
				<Col>
					<All onClick={() => handleClick("")}>
						<span>ALL</span>
						<span>전체</span>
					</All>
				</Col>
				<Col onClick={() => handleClick("어린이")}>
					<Icon>
						<SvgIcon component={ChildCareIcon} inheritViewBox fontSize="inherit" />
					</Icon>
					<span>어린이</span>
				</Col>
				<Col>
					<Icon onClick={() => handleClick("장애인")}>
						<SvgIcon component={AccessibleIcon} inheritViewBox fontSize="inherit" />
					</Icon>
					<span>장애인</span>
				</Col>
				<Col onClick={() => handleClick("노인")}>
					<Icon>
						<SvgIcon component={ElderlyIcon} inheritViewBox fontSize="inherit" />
					</Icon>
					<span>노인</span>
				</Col>
				<Col onClick={() => handleClick("동물")}>
					<Icon>
						<SvgIcon component={PetsIcon} inheritViewBox fontSize="inherit" />
					</Icon>
					<span>동물</span>
				</Col>
				<Col onClick={() => handleClick("환경")}>
					<Icon>
						<SvgIcon component={ForestIcon} inheritViewBox fontSize="inherit" />
					</Icon>
					<span>환경</span>
				</Col>
			</Flex>
		</Body>
	);
}

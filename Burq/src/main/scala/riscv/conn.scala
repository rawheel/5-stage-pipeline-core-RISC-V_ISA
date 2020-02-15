package riscv

import chisel3._
import chisel3.core.SInt
class conn extends Module{
	val io = IO(new Bundle{


		val output = Output(SInt(32.W))
		//val temp = Output(UInt(32.W))


})
	//modules
	//starting 5 stage registers
	val if_id_c = Module(new if_id_reg)
	val id_ex_c = Module(new id_ex_reg)
	val ex_mem_c = Module(new ex_mem_reg)
	val mem_wb_c = Module(new mem_wb_reg)
	val controlc = Module(new Control)
	val immediatec = Module(new Immediate)
	val reg32c = Module(new reg32)
	val aluc = Module(new alu)
	val aluconc = Module(new AluControl)
	val instruction_c = Module(new instruction)
	val pc_main = Module(new pc)
	//val jalr_c = Module(new jalr) //n
	val mem_c = Module(new main_mem)


	pc_main.io.input := pc_main.io.pc4
	//immediatec.io.pc := pc_main.io.pc_output

	instruction_c.io.write_address := pc_main.io.pc_output(11,2)
	controlc.io.OpCode :=if_id_c.io.instruction_output(6,0)

	//register

	reg32c.io.imm_rs1 := if_id_c.io.instruction_output(19,15)
	reg32c.io.imm_rs2 := if_id_c.io.instruction_output(24,20)
	//reg32c.io.rd_reg:= instruction_c.io.r_data(11,7)
	//reg32c.io.reg_write:=controlc.io.RegWrite

	//immediatec
	immediatec.io.instruction := if_id_c.io.instruction_output
	immediatec.io.pc := if_id_c.io.pc_output

	//5  stage AluControl
	aluconc.io.aluopp := controlc.io.AluOp
	aluconc.io.Func3:= if_id_c.io.instruction_output(14,12)
	aluconc.io.Func7:= if_id_c.io.instruction_output(30)






	//if_id_reg
	if_id_c.io.pc_input := pc_main.io.pc_output
	if_id_c.io.pc4_input := pc_main.io.pc4
	//val temp =instruction_c.io.r_data
	if_id_c.io.instruction_input := instruction_c.io.r_data

	//id_ex_reg

	id_ex_c.io.rs2_in := (reg32c.io.rs2_output).asUInt


	id_ex_c.io.rdsel := if_id_c.io.instruction_output(11,7)
	id_ex_c.io.cont_memwrite := controlc.io.MemWrite

	id_ex_c.io.cont_memread := controlc.io.MemRead
	id_ex_c.io.cont_regwrite := controlc.io.RegWrite
	id_ex_c.io.cont_memtoreg := controlc.io.MemToReg
	id_ex_c.io.alucont_in := aluconc.io.AluC

	//id_ex_c.io.cont_op := controlc.io.Operand_aSel

	//ALU
	 aluc.io.alu_c := id_ex_c.io.id_ex_alucont_out
	 aluc.io.A := (id_ex_c.io.cont_opA_out).asSInt
	 aluc.io.B := (id_ex_c.io.cont_opB_out).asSInt

	//ex_mem_reg
	ex_mem_c.io.cont_memwrite := id_ex_c.io.cont_memwrite
	ex_mem_c.io.cont_memread := id_ex_c.io.cont_memread
	ex_mem_c.io.cont_regwrite := id_ex_c.io.cont_regwrite_out
	ex_mem_c.io.id_ex_rs2in := id_ex_c.io.id_ex_rs2out
	ex_mem_c.io.id_ex_rdsel := id_ex_c.io.id_ex_rdsel_out
	ex_mem_c.io. memtoreg_in :=  id_ex_c.io.cont_memtoreg_out
	ex_mem_c.io.alu_out := (aluc.io.alu_output).asUInt

	//main main_mem
	mem_c.io.alu_out := (ex_mem_c.io.exme_alu_out(9,2)).asUInt
	mem_c.io.rs2 := (ex_mem_c.io.exme_rs2_out).asSInt
	mem_c.io.mem_write := ex_mem_c.io.exme_memwrit_out
	mem_c.io.mem_read := ex_mem_c.io.exme_memread_out

	//mem_wb_c
		mem_wb_c.io.memtoreg_in := ex_mem_c.io.memtoreg_out
		mem_wb_c.io.memwb_rdsel_in := ex_mem_c.io.exme_rdsel_out
		mem_wb_c.io.memwb_aluout_in := ex_mem_c.io.exme_alu_out
		mem_wb_c.io.memwb_dataout_in := (mem_c.io.mem_out).asUInt
		mem_wb_c.io.memwb_regwrite_in := ex_mem_c.io.exme_regwrite_out

	//writeback
	reg32c.io.rd_reg := mem_wb_c.io.mem_web_rdsel_out
	reg32c.io.reg_write := mem_wb_c.io.mem_web_regwrite_out

	//writeback
	//reg32c.io.write_data := (mem_wb_c.io.mem_web_aluout_out).asSInt

		when (mem_wb_c.io.mem_web_memtoreg_out=== 1.U){
			//reg32c.io.write_data := mem_c.io.mem_out
			reg32c.io.write_data := (mem_wb_c.io.mem_web_dataout_out).asSInt
		}.otherwise{
					when(reg32c.io.reg_write === 1.U){
						reg32c.io.write_data := (mem_wb_c.io.mem_web_aluout_out).asSInt
					}.otherwise{
						reg32c.io. write_data := 0.S
						//val temp = DontCare
					}

		}
		io.output := reg32c.io.write_data

		// operand A

			when(controlc.io.Operand_aSel==="b10".U){

				//luc.io.A := pc_main.io.pc4.asSInt
				id_ex_c.io.cont_opA := (if_id_c.io.pc4_output).asUInt

			}.elsewhen(controlc.io.Operand_aSel==="b01".U){

				//aluc.io.A := pc_main.io.pc_output.asSInt
				id_ex_c.io.cont_opA := (if_id_c.io.pc_output).asUInt

			}.otherwise{

				//aluc.io.A:=DontCare
					id_ex_c.io.cont_opA := (reg32c.io.rs1_output).asUInt

			}
		//Operand B
			when(controlc.io.ExtendSel ==="b00".U && controlc.io.Operand_bSel==="b1".U){

				id_ex_c.io.cont_opB := immediatec.io.I_Type.asUInt
			}.elsewhen(controlc.io.ExtendSel==="b10".U && controlc.io.Operand_bSel==="b1".U){
				id_ex_c.io.cont_opB:= immediatec.io.U_Type.asUInt

			}.elsewhen(controlc.io.ExtendSel==="b01".U && controlc.io.Operand_bSel==="b1".U){

				id_ex_c.io.cont_opB := immediatec.io.S_Type.asUInt

			}.otherwise{

				id_ex_c.io.cont_opB:= (reg32c.io.rs2_output).asUInt

			}

			when(controlc.io.ExtendSel ==="b00".U)
			{id_ex_c.io.imm := immediatec.io.I_Type.asUInt
			}.elsewhen(controlc.io.ExtendSel==="b10".U ){

				id_ex_c.io.imm:= immediatec.io.U_Type.asUInt
			}.elsewhen(controlc.io.ExtendSel==="b01".U ){
				id_ex_c.io.imm := immediatec.io.S_Type.asUInt
			}.otherwise
				{id_ex_c.io.imm:= 0.U(32.W)}



//FORWARDING
id_ex_c.io.id_ex_rs1_sel := if_id_c.io.instruction_output(19,15)
id_ex_c.io.id_ex_rs2_sel := if_id_c.io.instruction_output(24,20)

id_ex_c.io.pc := if_id_c.io.pc_output
id_ex_c.io.pc4 := if_id_c.io.pc4_output


val forwarding = Module(new forwarding)

forwarding.io.exmem_regwrite := ex_mem_c.io.exme_regwrite_out
forwarding.io.rdselexmem := ex_mem_c.io.exme_rdsel_out
forwarding.io.idexrs1 := id_ex_c.io.id_ex_rs1_out
forwarding.io.idex_rs2 := id_ex_c.io.id_ex_rs2_out
forwarding.io.memwb_regwrite := mem_wb_c.io.mem_web_regwrite_out
forwarding.io.memwb_rdsel := mem_wb_c.io.mem_web_rdsel_out

when (id_ex_c.io.cont_opA_out === "b10".U){
	aluc.io.A := id_ex_c.io.pc4_output.asSInt
	}.otherwise{
				when (forwarding.io.forwardA === "b00".U){
					aluc.io.A:= id_ex_c.io.id_ex_rs1_out.asSInt
				}.elsewhen(forwarding.io.forwardA === "b10".U){
					aluc.io.A:=reg32c.io.write_data
				}.elsewhen(forwarding.io.forwardA === "b01".U){
					aluc.io.A := ex_mem_c.io.exme_alu_out.asSInt
				}.otherwise(aluc.io.A := id_ex_c.io.id_ex_rs1_out.asSInt)
																																				}
when (id_ex_c.io.cont_opB_out === "b1".U){
	aluc.io.B := id_ex_c.io.imm_out.asSInt

					when (forwarding.io.forwardB === "b00".U){
						ex_mem_c.io.id_ex_rs2in:= id_ex_c.io.id_ex_rs2_out
					}.elsewhen (forwarding.io.forwardB === "b10".U){

						ex_mem_c.io.id_ex_rs2in := reg32c.io.write_data.asUInt


					}.elsewhen (forwarding.io.forwardB === "b01".U){
						ex_mem_c.io.id_ex_rs2in := ex_mem_c.io. exme_alu_out
					}.otherwise{ex_mem_c.io.id_ex_rs2in :=id_ex_c.io.id_ex_rs2_out}

}.otherwise{
		when (forwarding.io.forwardB === "b00".U){
			aluc.io.B:= id_ex_c.io.id_ex_rs2_out.asSInt
			ex_mem_c.io.id_ex_rs2in:= id_ex_c.io.id_ex_rs2_out

	}.elsewhen (forwarding.io.forwardB === "b10".U){
		aluc.io.B := reg32c.io.write_data
		ex_mem_c.io.id_ex_rs2in := reg32c.io.write_data.asUInt

	}.elsewhen (forwarding.io.forwardB === "b01".U){
		aluc.io.B := ex_mem_c.io.exme_alu_out.asSInt
		ex_mem_c.io.id_ex_rs2in := ex_mem_c.io. exme_alu_out
																									}

																															}

}




//WORKING FORWARDING

/*when (ex_mem_c.io.exme_regwrite_out === "b1".U && ex_mem_c.io.exme_rdsel_out =/= "b00000".U){
		when(ex_mem_c.io.exme_rdsel_out === id_ex_c.io.id_ex_rs1_out)
					{aluc.io.A := ex_mem_c.io.exme_alu_out.asSInt}

		.elsewhen(ex_mem_c.io.exme_rdsel_out === id_ex_c.io.id_ex_rs2_out)
					{aluc.io.B  := ex_mem_c.io.exme_alu_out.asSInt}
				}

when (mem_wb_c.io.mem_web_regwrite_out === "b1".U && mem_wb_c.io.mem_web_rdsel_out =/= "b00000".U){
		when(mem_wb_c.io.mem_web_rdsel_out === id_ex_c.io.id_ex_rs1_out)
					{aluc.io.A := mem_wb_c.io.mem_web_aluout_out.asSInt}
		.elsewhen(mem_wb_c.io.mem_web_rdsel_out === id_ex_c.io.id_ex_rs2_out)
					{aluc.io.B :=  mem_wb_c.io.mem_web_aluout_out.asSInt}
	}*/



//STALLING
/*iif_id_c. := if_id_c.io.instruction_output(19,15)
if_id_c. := if_id_c.io.instruction_output(24,20)
val stalling = Module(new stalling)
stalling.io.register_rd := */

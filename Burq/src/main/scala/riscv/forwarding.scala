package riscv

import chisel3._
import chisel3.util.Cat
import chisel3.util.Fill
import chisel3.core.SInt

class forwarding extends Module{
	val io = IO(new Bundle{



    val idexrs1 = Input(UInt(5.W))
    val idex_rs2 = Input(UInt(5.W))
    //val exmem_aluout = Input(UInt(32.W))
		val rdselexmem = Input(UInt(5.W))
		val memwb_rdsel = Input(UInt(5.W))

    val memwb_regwrite = Input(UInt(1.W))
		val exmem_regwrite = Input(UInt(1.W))

    //val memwb_aluout = Input(UInt(32.W))
		val forwardA = Output(UInt(2.W))
		val forwardB = Output(UInt(2.W))


})

io.forwardA := "b00".U
io.forwardB := "b00".U

when(io.exmem_regwrite==="b1".U && io.rdselexmem =/= "b00000".U && (io.rdselexmem===io.idexrs1) && (io.rdselexmem===io.idex_rs2) )
						{io.forwardA := "b01".U
						io.forwardB := "b01".U}
.elsewhen(io.exmem_regwrite==="b1".U && io.rdselexmem =/= "b00000".U && (io.rdselexmem===io.idexrs1))
  				{io.forwardA := "b01".U }

.elsewhen(io.exmem_regwrite==="b1".U && io.rdselexmem =/= "b00000".U && (io.rdselexmem===io.idex_rs2))
        {io.forwardB := "b01".U}



when(io.memwb_regwrite==="b1".U && io.memwb_rdsel =/= "b00000".U && (~(io.exmem_regwrite=== "b1".U && (io.rdselexmem =/= "b00000".U) && (io.rdselexmem === io.idexrs1)  && (io.rdselexmem===io.idex_rs2))) && (io.memwb_rdsel ===io.idexrs1)  && (io.memwb_rdsel===io.idex_rs2))
					{io.forwardA := "b10".U
					io.forwardB := "b10".U}

 .elsewhen(io.memwb_regwrite==="b1".U && io.memwb_rdsel =/= "b00000".U && (~(io.exmem_regwrite=== "b1".U && (io.rdselexmem =/= "b00000".U) && (io.rdselexmem === io.idexrs1))) && (io.memwb_rdsel ===io.idexrs1))
 					{io.forwardA := "b10".U}

 .elsewhen((io.memwb_regwrite === "b1".U) && (io.memwb_rdsel =/= "b00000".U) && (~(io.exmem_regwrite === "b1".U && (io.rdselexmem =/= "b00000".U) && (io.rdselexmem===io.idex_rs2))) && (io.memwb_rdsel===io.idex_rs2))
    { io.forwardB := "b10".U}
	}
